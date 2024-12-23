package br.com.pulsemc.minecraft.lobby.systems.scoreboard;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ScoreboardListener implements Listener {

    private final LobbyPlugin plugin;

    public ScoreboardListener(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        String title = plugin.getLanguageRegistry().getMessage(player, LanguagePath.SCOREBOARD_TITLE);
        List<String> lines = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.SCOREBOARD_LINES);

        plugin.getScoreboardManager().createScoreboard(player, title, lines);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        plugin.getScoreboardManager().removeScoreboard(player);
    }

    @EventHandler
    public void onPlayerLanguageChange(PlayerLanguageChangeEvent e) {
        Player player = e.getPlayer();

        plugin.getScoreboardManager().removeScoreboard(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            String title = plugin.getLanguageRegistry().getMessage(player, LanguagePath.SCOREBOARD_TITLE);
            List<String> lines = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.SCOREBOARD_LINES);

            plugin.getScoreboardManager().createScoreboard(player, title, lines);
            plugin.debug("ScoreboardListener.java - Nova scoreboard criada", true);
        }, 10L);
    }
}
