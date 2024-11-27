package br.com.pulsemc.minecraft.lobby.placeholder;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.model.PlayerData;
import br.com.pulsemc.minecraft.lobby.tools.Time;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlayerPlaceholders extends PlaceholderExpansion {

    private final Main plugin;

    public PlayerPlaceholders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "lobby";
    }

    @Override
    public String getAuthor() {
        return "tadeu";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return "";

        PlayerData playerData = plugin.getPlayerDataManager().loadPlayerData(player.getUniqueId().toString());
        if (playerData == null) return "";

        switch (params.toLowerCase()) {
            case "total_playtime":
                return Time.formatTime(playerData.getOnlineTime());
            case "first_join_date":
                return new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(playerData.getFirstJoin()));
            default:
                return "";
        }
    }
}