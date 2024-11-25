package br.com.pulsemc.minecraft.lobby.systems.language;

import lombok.Getter;

/** Enumeração que define os caminhos das traduções. */
@Getter
public enum LanguagePath {
    NO_PERMISSION("messages.no-permission"),
    PLAYER_NOT_FOUND("messages.player-not-found"),
    LANGUAGE_SELECTED("messages.language-selected"),
    LANGUAGE_NOT_FOUND("messages.language-not-found"),
    SCOREBOARD_TITLE("messages.scoreboard-title"),
    SCOREBOARD_LINES("messages.scoreboard-lines");

    private final String path;

    LanguagePath(String path) {
        this.path = path;
    }
}
