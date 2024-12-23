package br.com.pulsemc.minecraft.lobby.systems.tab;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.HeaderFooterManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TabManager implements br.com.pulsemc.minecraft.lobby.api.tab.TabAPI {

    private final Main plugin;
    private final HeaderFooterManager headerFooterManager;

    public TabManager(Main plugin) {
        this.plugin = plugin;
        this.headerFooterManager = TabAPI.getInstance().getHeaderFooterManager();
        startUpdater();
    }

    public void sendTab(Player player) {
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        if (tabPlayer == null || headerFooterManager == null) {
            return;
        }

        List<String> headerList = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.TAB_HEADER);
        List<String> footerList = plugin.getLanguageRegistry().getListMessage(player, LanguagePath.TAB_FOOTER);

        String header = String.join("\n", headerList);
        String footer = String.join("\n", footerList);

        headerFooterManager.setHeader(tabPlayer, header);
        headerFooterManager.setFooter(tabPlayer, footer);
    }

    public void resetTab(Player player) {
        TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

        if (tabPlayer == null || headerFooterManager == null) {
            return;
        }

        headerFooterManager.setHeader(tabPlayer, "");
        headerFooterManager.setFooter(tabPlayer, "");
    }

    private void startUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(player.getUniqueId());

                    if (tabPlayer == null || headerFooterManager == null) {
                        return;
                    }

                    List<String> headerList = plugin.getLanguageRegistry()
                            .getListMessage(player, LanguagePath.TAB_HEADER);
                    List<String> footerList = plugin.getLanguageRegistry()
                            .getListMessage(player, LanguagePath.TAB_FOOTER);

                    String header = String.join("\n", headerList);
                    String footer = String.join("\n", footerList);

                    headerFooterManager.setHeader(tabPlayer, header);
                    headerFooterManager.setFooter(tabPlayer, footer);
                });
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 40L);
    }
}