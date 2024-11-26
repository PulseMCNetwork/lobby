package br.com.pulsemc.minecraft.lobby.systems.lobby.items.manager;

import br.com.pulsemc.minecraft.lobby.Main;
import br.com.pulsemc.minecraft.lobby.systems.language.LanguageLocale;
import br.com.pulsemc.minecraft.lobby.tools.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbyItemManager {

    private final Main plugin;

    public LobbyItemManager(Main plugin) {
        this.plugin = plugin;
    }

    public void giveLobbyItems(Player player) {
        Configuration config = plugin.getConfig();
        ConfigurationSection itemsSection = config.getConfigurationSection("lobby-itens");

        if (itemsSection == null) return;

        LanguageLocale playerLocale = plugin.getLanguageRegistry().getPlayerLanguage(player);

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemConfig = itemsSection.getConfigurationSection(key);

            int slot = itemConfig.getInt("slot");
            String command = itemConfig.getString("command");
            Material material = Material.valueOf(itemConfig.getString("material"));
            int data = itemConfig.getInt("data");

            // Busca nome e lore no idioma do jogador
            String namePath = "lobby-itens." + key + ".name";
            String lorePath = "lobby-itens." + key + ".lore";

            String name = plugin.getMessagesConfiguration().getMessage(playerLocale, namePath);
            List<String> lore = plugin.getMessagesConfiguration().getMessageList(playerLocale, lorePath);

            ItemBuilder itemBuilder = new ItemBuilder(material, 1, (short) data)
                    .setName(name)
                    .setLore(lore);

            if (material == Material.SKULL_ITEM && data == 3) {
                String skullOwner = player.getName();
                itemBuilder.setSkullOwner(skullOwner);
            }

            ItemStack item = itemBuilder.create();

            NBTItem nbtItem = new NBTItem(item);
            nbtItem.setString("customCommand", command);
            item = nbtItem.getItem();

            player.getInventory().setItem(slot, item);
        }
    }
}
