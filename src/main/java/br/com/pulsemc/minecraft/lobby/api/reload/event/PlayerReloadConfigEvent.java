package br.com.pulsemc.minecraft.lobby.api.reload.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.time.LocalDateTime;

/**
 * Evento disparado quando um jogador recarrega as configurações do plugin.
 */
@Getter
public class PlayerReloadConfigEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player; // O jogador que recarregou as configurações
    private final LocalDateTime reloadTime; // O horário do recarregamento
    private final String commandLabel; // O comando usado para recarregar as configurações

    @Setter
    private boolean cancelled; // Permite cancelar o recarregamento das configurações

    /**
     * Construtor do evento.
     *
     * @param player       O jogador que recarregou as configurações.
     * @param reloadTime   O horário do recarregamento.
     * @param commandLabel O comando usado para acionar o recarregamento.
     */
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