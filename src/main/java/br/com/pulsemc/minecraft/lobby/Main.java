package br.com.pulsemc.minecraft.lobby;

import br.com.pulsemc.minecraft.lobby.configurations.Configuration;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    private Configuration configuration;

    @Override
    public void onEnable() {

        debug(" ", false);
        Stopwatch stopwatch = Stopwatch.createStarted();

        loadConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void debug(String message, boolean debug) {
        if (debug) {
            if (!getConfig().getBoolean("debug", true)) return;
            Bukkit.getConsoleSender().sendMessage("§8[LOBBY-DEBUG] §f" + message
                    .replace("&", "§"));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(message
                .replace("&", "§"));
    }

    private void loadConfiguration() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eCarregando configurações", false);

        configuration = new Configuration(this);

        debug("&aConfigurações carregadas em " + stopwatch.stop() + "!", false);
    }
}
