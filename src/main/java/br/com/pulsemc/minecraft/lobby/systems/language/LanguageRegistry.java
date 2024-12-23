package br.com.pulsemc.minecraft.lobby.systems.language;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.language.LanguageAPI;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import br.com.pulsemc.minecraft.lobby.database.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LanguageRegistry implements LanguageAPI {

    private final MySQLManager mySQLManager;
    private final Main plugin;
    private final Map<UUID, LanguageLocale> playerLanguageCache = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<UUID> pendingUpdates = new ConcurrentLinkedQueue<>();
    private BukkitRunnable databaseSyncTask;
    private boolean shuttingDown = false;

    public LanguageRegistry(Main plugin) {
        this.plugin = plugin;
        this.mySQLManager = plugin.getMySQLManager();
        createTable();
        startDatabaseSync();
    }

    private void createTable() {
        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_languages (" +
                             "uuid VARCHAR(36) PRIMARY KEY, " +
                             "language VARCHAR(10) NOT NULL)"
             )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("&cErro ao criar tabela de idiomas no MySQL: " + e.getMessage(), false);
        }
    }

    public void setPlayerLanguage(Player player, LanguageLocale locale) {
        if (shuttingDown) return;

        UUID playerUUID = player.getUniqueId();
        LanguageLocale oldLanguage = getPlayerLanguage(player);

        PlayerLanguageChangeEvent event = new PlayerLanguageChangeEvent(player, oldLanguage, locale);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        playerLanguageCache.put(playerUUID, locale);
        pendingUpdates.add(playerUUID);
    }

    public LanguageLocale getPlayerLanguage(Player player) {
        return getPlayerLanguage(player.getUniqueId());
    }

    public LanguageLocale getPlayerLanguage(UUID playerUUID) {
        if (playerLanguageCache.containsKey(playerUUID)) {
            return playerLanguageCache.get(playerUUID);
        }

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
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

        return LanguageLocale.PT_BR;
    }

    public String getMessage(Player player, LanguagePath path) {
        LanguageLocale locale = getPlayerLanguage(player);
        return plugin.getMessagesConfiguration().getMessage(locale, path.getPath());
    }

    public List<String> getListMessage(Player player, LanguagePath path) {
        LanguageLocale locale = getPlayerLanguage(player);
        return plugin.getMessagesConfiguration().getMessageList(locale, path.getPath());
    }

    private void startDatabaseSync() {
        databaseSyncTask = new BukkitRunnable() {
            @Override
            public void run() {
                syncPendingUpdates();
            }
        };
        databaseSyncTask.runTaskTimerAsynchronously(plugin, 0L, 36000L);
    }

    private void syncPendingUpdates() {
        Set<UUID> toUpdate = new HashSet<>();
        while (!pendingUpdates.isEmpty()) {
            UUID uuid = pendingUpdates.poll();
            if (uuid != null) {
                toUpdate.add(uuid);
            }
        }

        if (toUpdate.isEmpty()) return;

        try (Connection connection = mySQLManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "REPLACE INTO player_languages (uuid, language) VALUES (?, ?)"
             )) {
            List<UUID> batch = new ArrayList<>();
            for (UUID uuid : toUpdate) {
                LanguageLocale locale = playerLanguageCache.get(uuid);
                if (locale != null) {
                    statement.setString(1, uuid.toString());
                    statement.setString(2, locale.name());
                    statement.addBatch();
                    batch.add(uuid);
                }
                if (batch.size() >= 100) {
                    statement.executeBatch();
                    batch.clear();
                }
            }
            statement.executeBatch();
            plugin.debug("Idiomas sincronizados com sucesso na base de dados.", true);
        } catch (SQLException e) {
            plugin.debug("&cErro ao sincronizar idiomas no MySQL: " + e.getMessage(), false);
        }
    }

    public void clearCache() {
        syncPendingUpdates();
        playerLanguageCache.clear();
        plugin.debug("Cache de linguagens limpo com sucesso.", true);
    }

    public void onDisable() {
        shuttingDown = true;

        if (databaseSyncTask != null) {
            databaseSyncTask.cancel();
        }

        new Thread(this::clearCache).start();
    }
}