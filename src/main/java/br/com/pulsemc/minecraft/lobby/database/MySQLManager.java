package br.com.pulsemc.minecraft.lobby.database;

import br.com.pulsemc.minecraft.lobby.Main;
import lombok.Getter;
import lombok.var;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class MySQLManager {

    private final Main plugin;
    private Connection connection;

    public MySQLManager(Main plugin) {
        this.plugin = plugin;
        connect();
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            String host = plugin.getConfiguration().getConfig().getString("mysql.host");
            int port = plugin.getConfiguration().getConfig().getInt("mysql.port");
            String database = plugin.getConfiguration().getConfig().getString("mysql.database");
            String username = plugin.getConfiguration().getConfig().getString("mysql.username");
            String password = plugin.getConfiguration().getConfig().getString("mysql.password");
            String options = plugin.getConfiguration().getConfig().getString("mysql.options");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + options;

            connection = DriverManager.getConnection(url, username, password);
            plugin.debug("&aConexão com o banco de dados MySQL estabelecida.", false);
        } catch (SQLException e) {
            plugin.debug("&cErro ao conectar ao banco de dados MySQL: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    public void createTables() {
        try (Connection conn = getConnection();
             var statement = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "first_join TIMESTAMP, " +
                    "online_time BIGINT DEFAULT 0" +
                    ");";

            statement.executeUpdate(sql);
            plugin.debug("&aTabela player_data verificada/criada com sucesso.", false);

        } catch (SQLException e) {
            plugin.debug("&cErro ao criar/verificar tabela: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }


    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                plugin.debug("&cConexão com MySQL fechada, tentando reconectar...", false);
                connect();
            }
        } catch (SQLException e) {
            plugin.debug("&cErro ao verificar conexão com MySQL: " + e.getMessage(), false);
            e.printStackTrace();
        }
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.debug("&aConexão com o banco de dados MySQL encerrada.", false);
            } catch (SQLException e) {
                plugin.debug("&cErro ao encerrar conexão com MySQL: " + e.getMessage(), false);
                e.printStackTrace();
            }
        }
    }
}