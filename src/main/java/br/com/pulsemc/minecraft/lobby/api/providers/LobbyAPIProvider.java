package br.com.pulsemc.minecraft.lobby.api.providers;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.lobby.LobbyAPI;

public class LobbyAPIProvider {

    private static LobbyAPIProvider instance;
    private final LobbyAPI lobbyAPI;

    private LobbyAPIProvider(Main plugin) {
        this.lobbyAPI = plugin.getLobbyManager();
    }

    public static void initialize(Main plugin) {
        if (instance == null) {
            instance = new LobbyAPIProvider(plugin);
        }
    }

    public static LobbyAPI get() {
        if (instance == null) {
            throw new IllegalStateException("LobbyAPI não foi inicializada!");
        }
        return instance.lobbyAPI;
    }

    public static void shutdown() {
        if (instance != null) {
            instance = null;
        }
    }
}
