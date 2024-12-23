package br.com.pulsemc.minecraft.lobby.systems;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.model.PlayerData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDataManager {

    private final LobbyPlugin plugin;

    public PlayerDataManager(LobbyPlugin plugin) {
        this.plugin = plugin;
        plugin.getMySQLManager().createTables();
    }

    public PlayerData loadPlayerData(String uuid) {
        try (PreparedStatement statement = plugin.getMySQLManager().getConnection()
                .prepareStatement("SELECT * FROM player_data WHERE uuid = ?")) {

            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new PlayerData(
                        uuid,
                        resultSet.getTimestamp("first_join").getTime(),
                        resultSet.getLong("online_time")
                );
            } else {
                long currentTime = System.currentTimeMillis();
                savePlayerData(new PlayerData(uuid, currentTime, 0));
                return new PlayerData(uuid, currentTime, 0);
            }

        } catch (SQLException e) {
            plugin.debug("&cErro ao carregar dados do jogador: " + e.getMessage(), false);
            e.printStackTrace();
        }
        return null;
    }

    public void savePlayerData(PlayerData playerData) {
        try (PreparedStatement statement = plugin.getMySQLManager().getConnection()
                .prepareStatement("REPLACE INTO player_data (uuid, first_join, online_time) VALUES (?, ?, ?)")) {

            statement.setString(1, playerData.getUuid());
            statement.setTimestamp(2, new java.sql.Timestamp(playerData.getFirstJoin()));
            statement.setLong(3, playerData.getOnlineTime());
            statement.executeUpdate();

        } catch (SQLException e) {
            plugin.debug("&cErro ao salvar dados do jogador: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
}