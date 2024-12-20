package br.com.pulsemc.minecraft.lobby;

import br.com.pulsemc.minecraft.lobby.api.providers.LanguageAPIProvider;
import br.com.pulsemc.minecraft.lobby.commands.PulseLobbyCommand;
import br.com.pulsemc.minecraft.lobby.commands.language.LanguageCommand;
import br.com.pulsemc.minecraft.lobby.commands.lobby.BuildCommand;
import br.com.pulsemc.minecraft.lobby.commands.lobby.SetLobbyCommand;
import br.com.pulsemc.minecraft.lobby.configurations.Configuration;
import br.com.pulsemc.minecraft.lobby.configurations.MessagesConfiguration;
import br.com.pulsemc.minecraft.lobby.database.MySQLManager;
import br.com.pulsemc.minecraft.lobby.placeholder.PlayerPlaceholders;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageRegistry;
import br.com.pulsemc.minecraft.lobby.systems.language.listener.PlayerLanguageEvents;
import br.com.pulsemc.minecraft.lobby.systems.lobby.items.ItemInitializer;
import br.com.pulsemc.minecraft.lobby.systems.lobby.items.LobbyItemListener;
import br.com.pulsemc.minecraft.lobby.systems.lobby.items.LobbyItemManager;
import br.com.pulsemc.minecraft.lobby.systems.lobby.LobbyListener;
import br.com.pulsemc.minecraft.lobby.systems.lobby.LobbyManager;
import br.com.pulsemc.minecraft.lobby.systems.manager.PlayerDataManager;
import br.com.pulsemc.minecraft.lobby.systems.motd.MOTDListener;
import br.com.pulsemc.minecraft.lobby.systems.scoreboard.ScoreboardListener;
import br.com.pulsemc.minecraft.lobby.systems.scoreboard.ScoreboardManager;
import br.com.pulsemc.minecraft.lobby.systems.tab.TabListener;
import br.com.pulsemc.minecraft.lobby.systems.tab.TabManager;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    // Configurations
    private Configuration configuration;
    private MessagesConfiguration messagesConfiguration;

    // Database
    private MySQLManager mySQLManager;

    // Systems
    private PlayerDataManager playerDataManager; // Depends: MySQLManager
    private LanguageRegistry languageRegistry; // Depends: Configuration, MessagesConfiguration, MySQLManager
    private LobbyManager lobbyManager; // Depends: Configuration
    private ScoreboardManager scoreboardManager; // Depends: LanguageRegistry
    private TabManager tabManager; // Depends: LanguageRegistry e TAB
    private LobbyItemManager lobbyItemManager; // Depends: LanguageRegistry e NBT API

    @Override
    public void onEnable() {

        debug(" ", false);
        Stopwatch stopwatch = Stopwatch.createStarted();

        saveDefaultConfig();

        loadConfiguration();
        setupDatabase();

        setupMessages();
        loadManagers();

        loadListeners();
        loadCommands();

        initializeAPIs();

        debug("&2&lLobby-plugin iniciado em " + stopwatch.stop() + "!", false);
    }

    @Override
    public void onDisable() {

        LanguageAPIProvider.shutdown();

        if (mySQLManager != null) {
            mySQLManager.disconnect();
        }
    }

    public void debug(String message, boolean debug) {
        if (debug) {
            if (!getConfig().getBoolean("debug", true)) return;
            Bukkit.getConsoleSender().sendMessage("§8[LOBBY-DEBUG] §f" + message
                    .replace("&", "§"));
            return;
        }

        Bukkit.getConsoleSender().sendMessage(message
                .replace("&", "§"));
    }

    private void loadConfiguration() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eCarregando configurações...", false);

        configuration = new Configuration(this);
        messagesConfiguration = new MessagesConfiguration(this);

        debug("&aConfigurações carregadas em " + stopwatch.stop() + "!", false);
    }

    private void setupDatabase() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eConectando ao armazenamento...", false);

        this.mySQLManager = new MySQLManager(this);

        debug("&aArmazenamento conectado em " + stopwatch.stop() + "!", false);
    }

    private void setupMessages() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eCarregando sistema de mensagens...", false);

        this.languageRegistry = new LanguageRegistry(this);

        debug("&aMensagens carregadas em " + stopwatch.stop() + "!", false);

    }

    private void loadManagers() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eCarregando gerenciadores...", false);

        playerDataManager = new PlayerDataManager(this);
        lobbyManager = new LobbyManager(this);
        scoreboardManager = new ScoreboardManager(this);
        tabManager = new TabManager(this);
        lobbyItemManager = new LobbyItemManager(this);
        new ItemInitializer(this).initializeDefaultMessages();

        debug("&aGerenciadores carregados em " + stopwatch.stop() + "!", false);
    }

    private void loadListeners() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eRegistrando eventos...", false);

        registerListeners(
                new PlayerLanguageEvents(this),
                new LobbyListener(this),
                new ScoreboardListener(this),
                new TabListener(this),
                new MOTDListener(this),
                new LobbyItemListener(this)
        );

        debug("&aEventos registrados em " + stopwatch.stop() + "!", false);
    }

    private void loadCommands() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eRegistrando comandos...", false);

        LanguageCommand languageCommand = new LanguageCommand(this);
        PulseLobbyCommand pulseLobbyCommand = new PulseLobbyCommand(this);

        getCommand("language").setExecutor(languageCommand);
        getCommand("language").setTabCompleter(languageCommand);
        getCommand("setLobby").setExecutor(new SetLobbyCommand(this));
        getCommand("build").setExecutor(new BuildCommand(this));
        getCommand("pulseLobby").setExecutor(pulseLobbyCommand);
        getCommand("pulseLobby").setTabCompleter(pulseLobbyCommand);

        debug("&aComandos registrados em " + stopwatch.stop() + "!", false);

    }

    private void initializeAPIs() {

        Stopwatch stopwatch = Stopwatch.createStarted();
        debug(" ", false);
        debug("&eInicializando APIs...", false);

        LanguageAPIProvider.initialize(this);
        new PlayerPlaceholders(this).register();

        debug("&aAPIs inicializadas " + stopwatch.stop() + "!", false);
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
