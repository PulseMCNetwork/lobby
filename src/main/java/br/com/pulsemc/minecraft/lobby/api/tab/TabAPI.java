package br.com.pulsemc.minecraft.lobby.api.tab;

import org.bukkit.entity.Player;

public interface TabAPI {

    void sendTab(Player player);

    void resetTab(Player player);

}