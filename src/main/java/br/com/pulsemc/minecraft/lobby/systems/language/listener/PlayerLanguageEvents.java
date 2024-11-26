package br.com.pulsemc.minecraft.lobby.systems.language.listener;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLanguageEvents implements Listener {

    private final Main plugin;

    public PlayerLanguageEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        if (plugin.getLanguageRegistry().getPlayerLanguage(player) == null) {
            plugin.getLanguageRegistry().setPlayerLanguage(player, LanguageLocale.PT_BR);
            plugin.debug("Nenhuma linguagem para " + player.getName() + ", linguagem padrão selecionada", true);
        }
    }
}
