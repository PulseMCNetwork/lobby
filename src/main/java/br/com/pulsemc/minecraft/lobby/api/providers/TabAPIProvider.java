package br.com.pulsemc.minecraft.lobby.api.providers;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.api.tab.TabAPI;

public class TabAPIProvider {

    private static TabAPIProvider instance;
    private final TabAPI tabAPI;

    private TabAPIProvider(LobbyPlugin plugin) {
        this.tabAPI = plugin.getTabManager();
    }

    public static void initialize(LobbyPlugin plugin) {
        if (instance == null) {
            instance = new TabAPIProvider(plugin);
        }
    }

    public static TabAPI get() {
        if (instance == null) {
            throw new IllegalStateException("TabAPI não foi inicializada!");
        }
        return instance.tabAPI;
    }

    public static void shutdown() {
        if (instance != null) {
            instance = null;
        }
    }
}
