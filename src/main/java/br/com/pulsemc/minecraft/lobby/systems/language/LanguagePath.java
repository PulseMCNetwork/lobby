package br.com.pulsemc.minecraft.lobby.systems.language;

import lombok.Getter;

@Getter
public enum LanguagePath {
    NO_PERMISSION("messages.no-permission"),
    PLAYER_NOT_FOUND("messages.player-not-found"),
    LANGUAGE_SELECTED("messages.language-selected"),
    LANGUAGES_AVAILABLE("messages.languages-available"),
    LANGUAGE_NOT_FOUND("messages.language-not-found"),
    SCOREBOARD_TITLE("messages.scoreboard-title"),
    SCOREBOARD_LINES("messages.scoreboard-lines"),
    SCOREBOARD_DUEL_TITLE("messages.scoreboard-duel-title"),
    SCOREBOARD_DUEL_LINES("messages.scoreboard-duel-lines"),
    TAB_HEADER("messages.tab-title"),
    TAB_FOOTER("messages.tab-footer"),
    YOU_HAVE_PENDING_DUEL("messages.duels.you-already-have-pending-challenge"),
    PLAYER_HAVE_PENDING_DUEL("messages.duels.player-already-have-pending-challenge"),
    YOU_CHALLENGED_DUEL("messages.duels.you-challenged"),
    WERE_CHALLENGED_DUEL("messages.duels.you-were-challenged"),
    ACCEPT_BUTTON_DUEL("messages.duels.accept-button"),
    DENY_BUTTON_DUEL("messages.duels.deny-button"),
    YOUR_DUEL_EXPIRED("messages.duels.your-duel-challenge-expired"),
    SENT_DUEL_EXPIRED("messages.duel-challenge-sent-expired"),
    DONT_HAVE_DUEL("messages.duels.dont-have-any-duel"),
    YOU_DENIED_DUEL("messages.duels.you-denied-duel"),
    YOU_ACCEPT_DUEL("messages.duels.you-accept-duel"),
    OPPONENT_ACCEPTED_DUEL("messages.duels.opponent-accepted-duel"),
    PLAYER_LEFT_SERVER("messages.duels.player-left-server"),
    TITLE_WIN("messages.duels.title-win"),
    SUBTITLE_WIN("messages.duels.subtitle-win"),
    MESSAGE_WIN("messages.duels.message-win"),
    TITLE_LOST("messages.duels.title-lost"),
    SUBTITLE_LOST("messages.duels.subtitle-lost"),
    MESSAGE_LOST("messages.duels.message-lost"),
    KILLED_TO_MANY_TIMES("messages.duels.killed-too-many"),
    FLY_ENABLED("messages.fly.enabled"),
    FLY_DISABLED("messages.fly.disabled");

    private final String path;

    LanguagePath(String path) {
        this.path = path;
    }
}
