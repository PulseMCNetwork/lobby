package br.com.pulsemc.minecraft.lobby.systems.motd;

import br.com.pulsemc.minecraft.lobby.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class MOTDListener implements Listener {

    private final Main plugin;

    public MOTDListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        List<String> motdLore = plugin.getConfiguration().getConfig().getStringList("motd");

        if (motdLore.isEmpty()) {
            event.setMotd(ChatColor.RED + "MOTD não configurado no arquivo de configuração!");
            return;
        }

        String motd = motdLore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', centerText(line)))
                .reduce((line1, line2) -> line1 + "\n" + line2)
                .orElse(ChatColor.RED + "MOTD inválido!");

        event.setMotd(motd);
    }

    private String centerText(String message) {
        int CENTER_PX = 154;
        int messagePxSize = 0;
        boolean isBold = false;

        for (char c : ChatColor.stripColor(message).toCharArray()) {
            switch (c) {
                case 'i': case 'j': case 'l': case 't':
                    messagePxSize += isBold ? 5 : 4;
                    break;
                case 'f': case 'k': case 'I':
                    messagePxSize += isBold ? 6 : 5;
                    break;
                case 'm': case 'w': case 'M': case 'W':
                    messagePxSize += isBold ? 10 : 9;
                    break;
                default:
                    messagePxSize += isBold ? 7 : 6;
                    break;
            }
        }

        int toCompensate = CENTER_PX - messagePxSize / 2;
        int spaceLength = 4;
        int compensated = toCompensate / spaceLength;

        return repeat(" ", Math.max(0, compensated)) + message;
    }

    private String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }
}
