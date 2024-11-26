package br.com.pulsemc.minecraft.lobby.systems.lobby.listener;

import br.com.pulsemc.minecraft.lobby.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyListener implements Listener {

    private final Main plugin;

    public LobbyListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        e.setJoinMessage(null);

        if (plugin.getLobbyManager().getLobbyLocation() == null) {

            if (player.hasPermission("lobby.setup")) {
                player.sendMessage("§cO lobby não está setado!");
                player.sendMessage("§cUse '/setLobby' para setar o lobby.");
            }
            return;
        }

        plugin.getLobbyManager().teleportToLobby(player);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);

    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
    }
}
