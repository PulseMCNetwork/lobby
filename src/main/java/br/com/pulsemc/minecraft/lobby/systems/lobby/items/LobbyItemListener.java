package br.com.pulsemc.minecraft.lobby.systems.lobby.items;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import br.com.pulsemc.minecraft.lobby.commands.lobby.BuildCommand;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {

    private final Main plugin;

    public LobbyItemListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        player.getInventory().clear();

        plugin.getLobbyItemManager().giveLobbyItems(player);

        player.setCompassTarget(player.getLocation());

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (BuildCommand.playerCanBuild(player)) return;

        e.setCancelled(true);


        ItemStack item = e.getItem();

        if (item == null) return;

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("customCommand")) {
            String command = nbtItem.getString("customCommand");

            player.performCommand(command);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            if (BuildCommand.playerCanBuild(player)) return;

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            if (BuildCommand.playerCanBuild(player)) return;

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerLanguageChange(PlayerLanguageChangeEvent e) {

        Player player = e.getPlayer();

        player.getInventory().clear();

        Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getLobbyItemManager().giveLobbyItems(player), 10L);
    }
}
