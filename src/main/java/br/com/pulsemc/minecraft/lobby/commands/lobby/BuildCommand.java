package br.com.pulsemc.minecraft.lobby.commands.lobby;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class BuildCommand implements CommandExecutor {

    private final Main plugin;
    private static final Set<Player> buildModePlayers = new HashSet<>();

    public BuildCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.setup")) {
            player.sendMessage(plugin.getLanguageRegistry().getMessage(player, LanguagePath.NO_PERMISSION));
            return false;
        }

        if (buildModePlayers.contains(player)) {
            buildModePlayers.remove(player);
            player.sendMessage("§cVocê saiu do modo de construção!");
        } else {
            buildModePlayers.add(player);
            player.sendMessage("§aVocê entrou no modo de construção!");
        }

        plugin.debug("Modo de construção alterado de " + player.getName(), true);
        return true;
    }

    public static boolean playerCanBuild(Player player) {
        return buildModePlayers.contains(player);
    }
}
