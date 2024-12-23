package br.com.pulsemc.minecraft.lobby.api.providers;

import br.com.pulsemc.minecraft.lobby.LobbyPlugin;
import br.com.pulsemc.minecraft.lobby.api.lobby.LobbyItemAPI;

public class LobbyItemAPIProvider {

    private static LobbyItemAPIProvider instance;
    private final LobbyItemAPI lobbyItemAPI;

    private LobbyItemAPIProvider(LobbyPlugin plugin) {
        this.lobbyItemAPI = plugin.getLobbyItemManager();
    }

    public static void initialize(LobbyPlugin plugin) {
        if (instance == null) {
            instance = new LobbyItemAPIProvider(plugin);
        }
    }

    public static LobbyItemAPI get() {
        if (instance == null) {
            throw new IllegalStateException("LobbyItemAPI não foi inicializada!");
        }
        return instance.lobbyItemAPI;
    }

    public static void shutdown() {
        if (instance != null) {
            instance = null;
        }
    }
}
