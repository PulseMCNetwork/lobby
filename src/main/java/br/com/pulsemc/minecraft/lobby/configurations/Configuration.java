package br.com.pulsemc.minecraft.lobby.configurations;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class Configuration {

    private final LobbyPlugin plugin;
    private FileConfiguration config;

    public Configuration(LobbyPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getString(String path) {
        String message = config.getString(path);
        return message.replace("&", "§");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}
