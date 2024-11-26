package br.com.pulsemc.minecraft.lobby.systems.language;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.language.LanguageAPI;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import br.com.pulsemc.minecraft.lobby.database.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LanguageRegistry implements LanguageAPI {

    private final MySQLManager mySQLManager;
    private final Main plugin;
    private final Map<UUID, LanguageLocale> playerLanguageCache = new ConcurrentHashMap<>();

    public LanguageRegistry(Main plugin) {
        this.plugin = plugin;
        this.mySQLManager = plugin.getMySQLManager();
        createTable();
    }

    private void createTable() {
        try (PreparedStatement statement = mySQLManager.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS player_languages (" +
                        "uuid VARCHAR(36) PRIMARY KEY, " +
                        "language VARCHAR(10) NOT NULL" +
                        ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("&cErro ao criar tabela de idiomas no MySQL: " + e.getMessage(), false);
        }
    }

    public void setPlayerLanguage(Player player, LanguageLocale locale) {
        UUID playerUUID = player.getUniqueId();
        LanguageLocale oldLanguage = getPlayerLanguage(player);

        PlayerLanguageChangeEvent event = new PlayerLanguageChangeEvent(player, oldLanguage, locale);
        Bukkit.getPluginManager().callEvent(event);
        plugin.debug("Evento PlayerLanguageChangeEvent chamado para " + player.getName(), true);

        if (event.isCancelled()) {
            plugin.debug("Evento PlayerLanguageChangeEvent cancelado para " + player.getName(), true);
            return;
        }

        playerLanguageCache.put(playerUUID, locale);

        try (PreparedStatement statement = mySQLManager.getConnection().prepareStatement(
                "REPLACE INTO player_languages (uuid, language) VALUES (?, ?)"
        )) {
            statement.setString(1, playerUUID.toString());
            statement.setString(2, locale.name());
            statement.executeUpdate();
            plugin.debug("Linguagem alterada do jogador " + player.getName() + " para " + locale, true);
        } catch (SQLException e) {
            plugin.debug("&cErro ao salvar idioma no MySQL: " + e.getMessage(), false);
        }
    }

    public LanguageLocale getPlayerLanguage(Player player) {
        return getPlayerLanguage(player.getUniqueId());
    }

    public LanguageLocale getPlayerLanguage(UUID playerUUID) {
        if (playerLanguageCache.containsKey(playerUUID)) {
            return playerLanguageCache.get(playerUUID);
        }

        try (PreparedStatement statement = mySQLManager.getConnection().prepareStatement(
                "SELECT language FROM player_languages WHERE uuid = ?"
        )) {
            statement.setString(1, playerUUID.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LanguageLocale locale = LanguageLocale.fromString(resultSet.getString("language"));
                if (locale != null) {
                    playerLanguageCache.put(playerUUID, locale);
                    return locale;
                }
            }
        } catch (SQLException e) {
            plugin.debug("&cErro ao carregar idioma do MySQL: " + e.getMessage(), false);
        }

        return null;
    }

    public String getMessage(Player player, LanguagePath path) {
        LanguageLocale locale = getPlayerLanguage(player);
        return plugin.getMessagesConfiguration().getMessage(locale, path.getPath());
    }

    public List<String> getListMessage(Player player, LanguagePath path) {
        LanguageLocale locale = getPlayerLanguage(player);
        return plugin.getMessagesConfiguration().getMessageList(locale, path.getPath());
    }

    public void clearCache() {
        playerLanguageCache.clear();
        plugin.debug("Cache de linguagens limpo com sucesso.", true);
    }
}