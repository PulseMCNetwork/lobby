package br.com.pulsemc.minecraft.lobby.api.reload.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.LocalDateTime;

@Getter
public class PlayerReloadConfigEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final LocalDateTime reloadTime;
    private final String commandLabel;

    @Setter
    private boolean cancelled;

    public PlayerReloadConfigEvent(Player player, LocalDateTime reloadTime, String commandLabel) {
        this.player = player;
        this.reloadTime = reloadTime;
        this.commandLabel = commandLabel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}