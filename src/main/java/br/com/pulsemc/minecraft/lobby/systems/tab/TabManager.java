package br.com.pulsemc.minecraft.lobby.systems.tab;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.HeaderFooterManager;
import org.bukkit.entity.Player;

import java.util.List;

public class TabManager {

    private final Main plugin;
    private final HeaderFooterManager headerFooterManager;

    public TabManager(Main plugin) {
        this.plugin = plugin;
        this.headerFooterManager = TabAPI.getInstance().getHeaderFooterManager();
    }

    public void createTab(Player player) {
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        if (tabPlayer == null || headerFooterManager == null) {
            return;
        }

        // Obtém as mensagens da lista
        List<String> headerList = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.TAB_HEADER);
        List<String> footerList = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.TAB_FOOTER);

        // Junta os elementos das listas em uma única String, separados por uma nova linha
        String header = String.join("\n", headerList);
        String footer = String.join("\n", footerList);

        // Define o header e o footer
        headerFooterManager.setHeader(tabPlayer, header);
        headerFooterManager.setFooter(tabPlayer, footer);
    }

    public void resetTab(Player player) {
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        if (tabPlayer == null || headerFooterManager == null) {
            return;
        }

        // Resetar header e footer
        headerFooterManager.setHeader(tabPlayer, "");
        headerFooterManager.setFooter(tabPlayer, "");
    }
}
