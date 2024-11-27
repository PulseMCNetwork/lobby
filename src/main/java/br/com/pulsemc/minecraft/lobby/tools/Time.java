package br.com.pulsemc.minecraft.lobby.tools;

public class Time {

    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
    }
}
