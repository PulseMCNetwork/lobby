package br.com.pulsemc.minecraft.lobby.systems.lobby;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.commands.lobby.BuildCommand;
import br.com.pulsemc.minecraft.lobby.model.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class LobbyListener implements Listener {

    private final LobbyPlugin plugin;
    private final Map<String, Long> sessionStart = new HashMap<>();

    public LobbyListener(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        e.setJoinMessage(null);

        player.setHealth(20);
        player.getActivePotionEffects().clear();

        if (plugin.getLobbyManager().getLobbyLocation() == null) {

            if (player.hasPermission("lobby.setup")) {
                player.sendMessage("§cO lobby não está setado!");
                player.sendMessage("§cUse '/setLobby' para setar o lobby.");
            }
            return;
        }

        plugin.getLobbyManager().teleportToLobby(player);

        player.setGameMode(GameMode.ADVENTURE);

        String uuid = e.getPlayer().getUniqueId().toString();

        sessionStart.put(uuid, System.currentTimeMillis());

        plugin.getLobbyManager().setPlayerPvP(player, false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        e.setQuitMessage(null);

        String uuid = e.getPlayer().getUniqueId().toString();
        long startTime = sessionStart.getOrDefault(uuid, System.currentTimeMillis());

        PlayerData playerData = plugin.getPlayerDataManager().loadPlayerData(uuid);
        if (playerData != null) {
            long sessionTime = System.currentTimeMillis() - startTime;
            playerData.setOnlineTime(playerData.getOnlineTime() + sessionTime);
            plugin.getPlayerDataManager().savePlayerData(playerData);
        }

        sessionStart.remove(uuid);

        if (plugin.getLobbyManager().getCanPvP().contains(e.getPlayer())) {
            plugin.getLobbyManager().getCanPvP().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        if (plugin.getLobbyManager().getCanPvP().contains((Player) e.getEntity())) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!BuildCommand.playerCanBuild(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!BuildCommand.playerCanBuild(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (!BuildCommand.playerCanBuild(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent e) {
        if (!BuildCommand.playerCanBuild(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    // SERVIDOR
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
    }
}
