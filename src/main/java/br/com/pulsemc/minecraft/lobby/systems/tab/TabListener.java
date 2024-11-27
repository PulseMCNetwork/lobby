package br.com.pulsemc.minecraft.lobby.systems.tab;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListener implements Listener {

    private final Main plugin;

    public TabListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        plugin.getTabManager().createTab(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        Player player = e.getPlayer();

        plugin.getTabManager().resetTab(player);
    }

    @EventHandler
    public void onPlayerLanguageChange(PlayerLanguageChangeEvent e) {

        Player player = e.getPlayer();

        plugin.getTabManager().resetTab(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getTabManager().createTab(player);
            plugin.debug("TabListener.java - Novo tab criado", true);
        }, 10L);
    }
}
