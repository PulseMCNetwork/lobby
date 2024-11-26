package br.com.pulsemc.minecraft.lobby.systems.lobby.items.initializer;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.configurations.MessagesConfiguration;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import org.bukkit.configuration.ConfigurationSection;

public class ItemInitializer {

    private final Main plugin;

    public ItemInitializer(Main plugin) {
        this.plugin = plugin;
    }

    public void initializeDefaultMessages() {
        MessagesConfiguration messagesConfig = plugin.getMessagesConfiguration();

        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("lobby-itens");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                String namePath = "lobby-itens." + key + ".name";
                String lorePath = "lobby-itens." + key + ".lore";

                String defaultName = "&cconfigure aqui: " + namePath;
                String defaultLore = "&cconfigure aqui: " + lorePath;

                // Adiciona mensagens padrão para cada idioma
                for (LanguageLocale locale : LanguageLocale.values()) {
                    messagesConfig.addDefaultMessage(locale, namePath, defaultName);
                    messagesConfig.addDefaultMessage(locale, lorePath, defaultLore);
                }
            }
        }
    }
}
