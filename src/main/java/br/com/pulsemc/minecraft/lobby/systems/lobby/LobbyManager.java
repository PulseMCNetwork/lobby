package br.com.pulsemc.minecraft.lobby.systems.lobby;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.api.lobby.LobbyAPI;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class LobbyManager implements LobbyAPI {

    private final LobbyPlugin plugin;
    @Getter
    private Set<Player> canPvP = new HashSet<>();

    public LobbyManager(LobbyPlugin plugin) {
        this.plugin = plugin;
    }

    public void setLobbyLocation(Location location) {
        plugin.getConfig().set("lobby.world", location.getWorld().getName());
        plugin.getConfig().set("lobby.x", location.getX());
        plugin.getConfig().set("lobby.y", location.getY());
        plugin.getConfig().set("lobby.z", location.getZ());
        plugin.getConfig().set("lobby.yaw", location.getYaw());
        plugin.getConfig().set("lobby.pitch", location.getPitch());
        plugin.saveConfig();
    }

    public Location getLobbyLocation() {

        String worldName = plugin.getConfig().getString("lobby.world");
        if (worldName == null) {
            return null;
        }

        double x = plugin.getConfig().getDouble("lobby.x");
        double y = plugin.getConfig().getDouble("lobby.y");
        double z = plugin.getConfig().getDouble("lobby.z");
        float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
        float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

        return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void teleportToLobby(Player player) {
        String worldName = plugin.getConfig().getString("lobby.world");
        if (worldName == null) {
            player.sendMessage("§cO lobby não foi definido ainda.");
            return;
        }

        double x = plugin.getConfig().getDouble("lobby.x");
        double y = plugin.getConfig().getDouble("lobby.y");
        double z = plugin.getConfig().getDouble("lobby.z");
        float yaw = (float) plugin.getConfig().getDouble("lobby.yaw");
        float pitch = (float) plugin.getConfig().getDouble("lobby.pitch");

        Location lobbyLocation = new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
        player.teleport(lobbyLocation);
    }

    public void setPlayerPvP(Player player, boolean playerCanPvP) {

        if (playerCanPvP) {

            if (canPvP.contains(player)) return;

            canPvP.add(player);

        } else {

            if (!canPvP.contains(player)) return;

            canPvP.remove(player);
        }
    }

    public boolean playerCanPvP(Player player) {
        return canPvP.contains(player);
    }
}
