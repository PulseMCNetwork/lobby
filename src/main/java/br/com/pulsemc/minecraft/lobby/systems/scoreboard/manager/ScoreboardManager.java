package br.com.pulsemc.minecraft.lobby.systems.scoreboard.manager;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageRegistry;
import br.com.pulsemc.minecraft.lobby.tools.Text;
import fr.mrmicky.fastboard.FastBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public ScoreboardManager(Main plugin) {
        LanguageRegistry languageRegistry = plugin.getLanguageRegistry();
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
}