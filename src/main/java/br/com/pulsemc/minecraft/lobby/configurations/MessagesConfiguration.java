package br.com.pulsemc.minecraft.lobby.configurations;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MessagesConfiguration {

    private final Main plugin;
    private final File messagesPtFile;
    private final File messagesEnFile;
    private FileConfiguration messagesPtConfig;
    private FileConfiguration messagesEnConfig;

    public MessagesConfiguration(Main plugin) {
        this.plugin = plugin;

        // Define os arquivos de mensagens
        this.messagesPtFile = new File(plugin.getDataFolder(), "messages_pt.yml");
        this.messagesEnFile = new File(plugin.getDataFolder(), "messages_en.yml");

        // Carrega os arquivos de mensagens
        loadMessagesFile(messagesPtFile, "messages_pt.yml");
        loadMessagesFile(messagesEnFile, "messages_en.yml");

        this.messagesPtConfig = YamlConfiguration.loadConfiguration(messagesPtFile);
        this.messagesEnConfig = YamlConfiguration.loadConfiguration(messagesEnFile);
    }

    private void loadMessagesFile(File file, String resourceName) {
        if (!file.exists()) {
            plugin.saveResource(resourceName, false);
        }
    }

    public String getMessage(LanguageLocale locale, String path) {
        FileConfiguration config = getConfigForLocale(locale);
        String message = config.getString(path, "§cMensagem não encontrada.");
        return message.replace("&", "§");
    }

    public List<String> getMessageList(LanguageLocale locale, String path) {
        FileConfiguration config = getConfigForLocale(locale);
        List<String> messages = config.getStringList(path);
        if (messages.isEmpty()) {
            return Collections.singletonList("§cLista de mensagens não encontrada.");
        }
        return messages.stream().map(line -> line.replace("&", "§")).collect(Collectors.toList());
    }

    private FileConfiguration getConfigForLocale(LanguageLocale locale) {
        switch (locale) {
            case PT_BR:
                return messagesPtConfig;
            case EN_US:
                return messagesEnConfig;
            default:
                throw new IllegalArgumentException("Locale não suportado: " + locale);
        }
    }

    public void reloadMessages() {
        this.messagesPtConfig = YamlConfiguration.loadConfiguration(messagesPtFile);
        this.messagesEnConfig = YamlConfiguration.loadConfiguration(messagesEnFile);
    }
}