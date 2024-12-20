package br.com.pulsemc.minecraft.lobby.api.providers;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.language.LanguageAPI;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageRegistry;

/**
 * Provedor da API de linguagem. Mantém uma instância singleton segura.
 */
public class LanguageAPIProvider {

    private static LanguageAPIProvider instance;
    private final LanguageAPI languageAPI;

    private LanguageAPIProvider(Main plugin) {
        this.languageAPI = plugin.getLanguageRegistry();
    }

    public static void initialize(Main plugin) {
        if (instance == null) {
            instance = new LanguageAPIProvider(plugin);
        }
    }

    public static LanguageAPI get() {
        if (instance == null) {
            throw new IllegalStateException("LanguageAPI não foi inicializada!");
        }
        return instance.languageAPI;
    }

    public static void shutdown() {
        if (instance != null) {
            ((LanguageRegistry) instance.languageAPI).clearCache();
            instance = null;
        }
    }
}
