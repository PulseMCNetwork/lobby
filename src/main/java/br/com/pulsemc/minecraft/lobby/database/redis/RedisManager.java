package br.com.pulsemc.minecraft.lobby.database.redis;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
public class RedisManager {

    private final LobbyPlugin plugin;
    private RedisClient redisClient;
    private StatefulRedisPubSubConnection<String, String> pubSubConnection;

    public RedisManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        String host = plugin.getConfig().getString("redis.host");
        int port = plugin.getConfig().getInt("redis.port");
        String password = plugin.getConfig().getString("redis.password");

        if (host == null || host.isEmpty() || password == null || password.isEmpty()) {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        String redisUrl = String.format("redis://%s@%s:%d", password, host, port);

        try {
            this.redisClient = RedisClient.create(redisUrl);
            this.pubSubConnection = redisClient.connectPubSub();

        } catch (Exception e) {
            plugin.debug("§cFalha ao conectar ao servidor Redis: " + e.getMessage(), false);
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void subscribe(String channel, RedisMessageListener listener) {
        if (pubSubConnection != null) {
            pubSubConnection.addListener(listener);
            pubSubConnection.sync().subscribe(channel);
            plugin.debug("§aInscrição ao canal redis : " + channel, true);
        } else {
            plugin.debug("§cFalha ao se inscrever no Redis, Conexão redis não está estável", false);
        }
    }

    public void publish(String channel, String message) {
        if (pubSubConnection != null) {
            pubSubConnection.sync().publish(channel, message);
        } else {
            plugin.debug("§cFalha ao publicar mensagem no Redis, Conexão redis não está estável", false);
        }
    }

    public void shutdown() {
        if (pubSubConnection != null) {
            pubSubConnection.close();
        }

        if (redisClient != null) {
            redisClient.shutdown();
        }
    }
}