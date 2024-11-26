package br.com.pulsemc.minecraft.lobby.commands.language;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.api.providers.LanguageAPIProvider;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public LanguageCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return false;
        }

        Player player = (Player) sender;

        // Caso o jogador não forneça argumentos, mostre a lista de idiomas disponíveis
        if (args.length == 0) {
            List<String> messages = LanguageAPIProvider.get()
                    .getListMessage(player, LanguagePath.LANGUAGE_NOT_FOUND);
            for (String message : messages) {
                player.sendMessage(message);
            }
            return true;
        }

        // Valida o idioma fornecido
        if (args.length == 1) {
            String iso = args[0].toUpperCase();
            LanguageLocale locale = LanguageLocale.fromString(iso);

            if (locale == null) {
                // Idioma inválido, mostra mensagem de erro
                List<String> messages = LanguageAPIProvider.get()
                        .getListMessage(player, LanguagePath.LANGUAGES_AVAILABLE);
                for (String message : messages) {
                    player.sendMessage(message);
                }
                return true;
            }

            // Define o novo idioma do jogador
            LanguageAPIProvider.get().setPlayerLanguage(player, locale);

            // Envia mensagem confirmando a troca de idioma
            String successMessage = LanguageAPIProvider.get()
                    .getMessage(player, LanguagePath.LANGUAGE_SELECTED)
                    .replace("{LANGUAGE}", locale.name());
            player.sendMessage(successMessage);
            plugin.debug("Linguagem alterada de " + player.getName() + " para: " + locale, true);
            return true;
        }

        // Argumentos inválidos
        player.sendMessage("§cUse: /language <iso>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return getAvailableLocales().stream()
                    .filter(locale -> locale.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private List<String> getAvailableLocales() {
        List<String> locales = new ArrayList<>();
        for (LanguageLocale locale : LanguageLocale.values()) {
            locales.add(locale.name());
            locales.add(locale.name().split("_")[0]);
        }
        return locales;
    }
}