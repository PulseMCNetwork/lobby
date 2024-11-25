package br.com.pulsemc.minecraft.lobby.database;

import br.com.pulsemc.minecraft.lobby.Main;
import lombok.Getter;

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

    private void connect() {
        try {
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