package br.com.pulsemc.minecraft.lobby.tools;

import java.util.List;
import java.util.stream.Collectors;

/*
@author: syncwrld
@website: https://github.com/syncwrld
 */
public class Text {

    public static String color(String text) {
        return text.replaceAll("&", "§");
    }

    public static List<String> color(List<String> text) {
        return text.stream().map(Text::color).collect(Collectors.toList());
    }

    public static List<String> replace(List<String> text, String ago, String now) {
        return text.stream().map((s) -> s.replace(ago, now)).collect(Collectors.toList());
    }

    public static String jumpLines(List<String> text) {
        return String.join("\n ", text);
    }

    public static String jumpLines(String[] array) {
        return String.join("\n ", array);
    }
}
