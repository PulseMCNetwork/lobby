package br.com.pulsemc.minecraft.lobby.systems.lobby.items;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.api.language.events.PlayerLanguageChangeEvent;
import br.com.pulsemc.minecraft.lobby.commands.lobby.BuildCommand;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyItemListener implements Listener {

    private final LobbyPlugin plugin;

    public LobbyItemListener(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        player.getInventory().clear();

        player.getInventory().setArmorContents(null);

        plugin.getLobbyItemManager().giveLobbyItems(player);

        player.setCompassTarget(player.getLocation());

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (BuildCommand.playerCanBuild(player)) return;

        if (plugin.getLobbyManager().playerCanPvP(player)) return;

        e.setCancelled(true);

        ItemStack item = e.getItem();

        if (item == null) return;

        String tag = NBT.get(item, (nbt) -> {
            return nbt.getString("customCommand");
        });

        if (tag != null) {

            if (tag.contains("{TARGET}")) return;

            player.performCommand(tag.replace("{PLAYER}", player.getName()));

            plugin.debug(player.getName() + " executou o comando: " + tag.replace("{PLAYER}", player.getName()), true);

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

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;

        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getDamager();

        if (BuildCommand.playerCanBuild(player)) return;

        Player target = (Player) e.getEntity();
        ItemStack item = player.getInventory().getItemInHand();

        if (item == null || item.getType() == Material.AIR || target == null) return;

        NBTItem nbtItem = new NBTItem(item);

        if (nbtItem.hasTag("customCommand")) {
            String command = nbtItem.getString("customCommand").replace("{PLAYER}", player.getName());

            command = command.replace("{TARGET}", target.getName());

            player.performCommand(command);
            plugin.debug(player.getName() + " executou o comando: " + command, true);

            e.setCancelled(true);
        }
    }
}