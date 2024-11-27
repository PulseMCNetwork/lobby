package br.com.pulsemc.minecraft.lobby.systems.scoreboard;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageRegistry;
import br.com.pulsemc.minecraft.lobby.tools.Text;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Main plugin;
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        LanguageRegistry languageRegistry = plugin.getLanguageRegistry();

        startUpdater();
    }

    public void createScoreboard(Player player, String title, List<String> lines) {

        FastBoard board = new FastBoard(player);

        board.updateTitle(Text.color(PlaceholderAPI.setPlaceholders(player, title)));

        board.updateLines(Text.color(PlaceholderAPI.setPlaceholders(player, lines)));

        boards.put(player.getUniqueId(), board);
    }

    public void removeScoreboard(Player player) {

        FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) {
            board.delete();
        }
    }

    private void startUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (UUID uuid : boards.keySet()) {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null || !player.isOnline()) {
                    assert player != null;
                    removeScoreboard(player);
                    continue;
                }

                updateScoreboard(player);
            }
        }, 0L, 20L);
    }

    private void updateScoreboard(Player player) {
        FastBoard board = boards.get(player.getUniqueId());
        if (board == null) return;

        board.updateTitle(Text.color(PlaceholderAPI.setPlaceholders(player, board.getTitle())));
        board.updateLines(Text.color(PlaceholderAPI.setPlaceholders(player, board.getLines())));
    }
}