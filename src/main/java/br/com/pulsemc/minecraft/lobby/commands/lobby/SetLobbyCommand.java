package br.com.pulsemc.minecraft.lobby.commands.lobby;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {

    private final Main plugin;

    public SetLobbyCommand(Main plugin) {
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

        plugin.getLobbyManager().setLobbyLocation(player.getLocation());

        plugin.debug("Lobby setado por " + player.getName(), true);
        player.sendMessage("§aLobby setado na sua atual posição!");
        return true;
    }
}
