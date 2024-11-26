package br.com.pulsemc.minecraft.lobby.commands;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PulseLobbyCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public PulseLobbyCommand(Main plugin) {
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

        if (args.length == 0) {
            sendHelp(player);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfiguration().reloadConfig();
            plugin.getMessagesConfiguration().reloadMessages();
            player.sendMessage("§aConfigurações recarregadas!");
            return true;
        }

        return false;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§5§lPulseLobby §f§lCommands");
        player.sendMessage("");
        player.sendMessage("§5/pulseLobby reload");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1 && sender.hasPermission("lobby.setup")) {
                suggestions.add("reload");
            }

        return suggestions;
    }
}
