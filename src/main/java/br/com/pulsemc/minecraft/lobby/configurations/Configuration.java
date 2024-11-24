package br.com.pulsemc.minecraft.lobby.configurations;

import br.com.pulsemc.minecraft.lobby.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class Configuration {

    private final Main plugin;
    private FileConfiguration config;

    public Configuration(Main plugin) {
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
