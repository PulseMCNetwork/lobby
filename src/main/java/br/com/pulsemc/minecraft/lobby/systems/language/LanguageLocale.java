package br.com.pulsemc.minecraft.lobby.systems.language;

/** Enumeração que representa os locais de linguagem disponíveis. */
public enum LanguageLocale {
    PT_BR,
    EN_US;

    public static LanguageLocale fromString(String locale) {
        try {
            return LanguageLocale.valueOf(locale.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
