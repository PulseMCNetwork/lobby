package br.com.pulsemc.minecraft.lobby.api.lobby;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface LobbyAPI {

    void setLobbyLocation(Location location);

    Location getLobbyLocation();

    void teleportToLobby(Player player);

    void setPlayerPvP(Player player, boolean playerCanPvP);

    boolean playerCanPvP(Player player);
}
