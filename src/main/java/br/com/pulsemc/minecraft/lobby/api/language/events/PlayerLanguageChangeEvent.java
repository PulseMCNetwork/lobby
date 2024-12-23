package br.com.pulsemc.minecraft.lobby.api.language.events;

import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerLanguageChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final LanguageLocale oldLanguage;

    @Setter
    private LanguageLocale newLanguage;

    @Setter
    private boolean cancelled;

    public PlayerLanguageChangeEvent(Player player, LanguageLocale oldLanguage, LanguageLocale newLanguage) {
        this.player = player;
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}