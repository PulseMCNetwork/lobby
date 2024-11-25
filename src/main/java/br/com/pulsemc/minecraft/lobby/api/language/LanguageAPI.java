package br.com.pulsemc.minecraft.lobby.api.language;

import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguagePath;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * API para gerenciamento de idiomas no servidor.
 */
public interface LanguageAPI {

    /**
     * Define a linguagem preferida de um jogador.
     *
     * @param player O jogador.
     * @param locale A nova linguagem.
     */
    void setPlayerLanguage(Player player, LanguageLocale locale);

    /**
     * Obtém a linguagem preferida de um jogador.
     *
     * @param player O jogador.
     * @return A linguagem preferida do jogador.
     */
    LanguageLocale getPlayerLanguage(Player player);

    /**
     * Obtém a linguagem preferida de um jogador pelo UUID.
     *
     * @param playerUUID O UUID do jogador.
     * @return A linguagem preferida do jogador.
     */
    LanguageLocale getPlayerLanguage(UUID playerUUID);

    /**
     * Obtém uma mensagem traduzida para o jogador.
     *
     * @param player O jogador.
     * @param path   O caminho da mensagem.
     * @return A mensagem traduzida.
     */
    String getMessage(Player player, LanguagePath path);

    /**
     * Obtém uma lista de mensagens traduzidas para o jogador.
     *
     * @param player O jogador.
     * @param path   O caminho das mensagens.
     * @return A lista de mensagens traduzidas.
     */
    List<String> getListMessage(Player player, LanguagePath path);
}
