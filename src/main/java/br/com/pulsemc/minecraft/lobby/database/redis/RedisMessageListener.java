package br.com.pulsemc.minecraft.lobby.database.redis;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.lettuce.core.pubsub.RedisPubSubListener;
import org.bukkit.Bukkit;

public class RedisMessageListener implements RedisPubSubListener<String, String> {

    private final LobbyPlugin plugin;
    private final Gson gson;

    public RedisMessageListener(LobbyPlugin plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    @Override
    public void message(String channel, String message) {
        plugin.debug("§aMensagem recebida do canal: " + channel + ", mensagem: " + message, true);

        try {
            JsonObject jsonMessage = gson.fromJson(message, JsonObject.class);
            String action = jsonMessage.get("action").getAsString();

            if (action != null) {
                Bukkit.getScheduler().runTask(plugin, () -> handleAction(action, jsonMessage));
            }
        } catch (JsonSyntaxException e) {
            plugin.debug("§cFalha ao receber mensagem: " + message, false);
            e.printStackTrace();
        }
    }

    private void handleAction(String action, JsonObject jsonMessage) {
        switch (action.toLowerCase()) {
            case "create":
                handleCreate(jsonMessage);
                break;

            case "invalidate":
                handleInvalidate(jsonMessage);
                break;

            default:
                plugin.debug("§cAção desconhecida: " + action, false);
        }
    }

    private void handleCreate(JsonObject jsonMessage) {
        plugin.getLogger().info("Handling 'create' action.");
        // Código desnecessário
    }

    private void handleInvalidate(JsonObject jsonMessage) {
        plugin.getLogger().info("Handling 'invalidate' action.");
        // Código desnecessário
    }

    @Override
    public void message(String channel, String key, String message) {
        message(channel, message);
    }

    @Override
    public void subscribed(String channel, long count) {
        plugin.getLogger().info("Subscribed to channel: " + channel);
    }

    @Override
    public void psubscribed(String pattern, long count) {
        // Código desnecessário
    }

    @Override
    public void unsubscribed(String channel, long count) {
        plugin.getLogger().info("Unsubscribed from channel: " + channel);
    }

    @Override
    public void punsubscribed(String pattern, long count) {
        // Código desnecessário
    }
}