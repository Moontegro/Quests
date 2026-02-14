package com.moontegro.quests.utils.menu;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.utils.color.Color;
import com.moontegro.quests.utils.menu.holders.QuestInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public interface PaginatedMenu {

    Component title();
    int size();
    Map<UUID, Integer> pages();

    default void open(Player player) {

        int currentPage = pages().computeIfAbsent(player.getUniqueId(), uuid -> 1);

        Inventory inventory = Bukkit.createInventory(
                new QuestInventoryHolder(),
                size(),
                title()
        );

        Map<Integer, ItemStack> items = fillItems(player, currentPage);
        items.forEach(inventory::setItem);

        if (currentPage > 1) {
            inventory.setItem(size() - 9, previousPage());
        }

        if (canFill(currentPage + 1)) {
            inventory.setItem(size() - 1, nextPage());
        }

        player.openInventory(inventory);
    }

    default boolean canFill(int page) {
        int itemsPerPage = slots().size();
        int startIndex = (page - 1) * itemsPerPage;

        return Quests.getInstance()
                .getQuestManager()
                .getQuests()
                .size() > startIndex;
    }

    default Map<Integer, ItemStack> fillItems(Player player, int page) {

        List<Quest<?>> quests = new ArrayList<>(
                Quests.getInstance().getQuestManager().getQuests().values()
        );

        List<Integer> slots = slots();
        Map<Integer, ItemStack> inventorySlots = new HashMap<>();

        int start = (page - 1) * slots.size();
        int end = Math.min(start + slots.size(), quests.size());

        for (int i = start; i < end; i++) {

            Quest<?> quest = quests.get(i);
            int slot = slots.get(i - start);

            ItemStack itemStack = new ItemStack(quest.getGuiMaterial());
            ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                meta.displayName(Color.translate(quest.getGuiItemName()));
                int progress = 5;
                int required = quest.getAmount();

                double percentDouble = (double) progress / required;
                int percent = (int) (percentDouble * 100);

                int barLength = 5; // total blocks in bar
                int filledBars = (int) Math.round(percentDouble * barLength);

                StringBuilder bar = new StringBuilder("&8[");

                for (int x = 0; x < barLength; x++) {
                    if (x < filledBars) {
                        bar.append("&a■");
                    } else {
                        bar.append("&7□");
                    }
                }

                bar.append("&8]");

                List<Component> lore = new ArrayList<>();

                for (String l : quest.getGuiLore()) {

                    String replaced = l
                            .replace("%progress%", String.valueOf(progress))
                            .replace("%required%", String.valueOf(required))
                            .replace("%percentage%", bar + " &7" + percent + "%");

                    lore.add(Color.translate(replaced));
                }

                meta.lore(lore);

                meta.lore(lore);
                itemStack.setItemMeta(meta);
            }

            inventorySlots.put(slot, itemStack);
        }

        return inventorySlots;
    }

    default List<Integer> slots() {
        return Quests.getInstance()
                .getConfiguration()
                .getConfiguration()
                .getIntegerList("quest-inventory.available-slots");
    }

    default ItemStack nextPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Color.translate("&aNext Page"));
        meta.getPersistentDataContainer().set(
                Quests.getInstance().getButton(),
                PersistentDataType.STRING,
                "next_page"
        );

        item.setItemMeta(meta);
        return item;
    }

    default ItemStack previousPage() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Color.translate("&cPrevious Page"));
        meta.getPersistentDataContainer().set(
                Quests.getInstance().getButton(),
                PersistentDataType.STRING,
                "previous_page"
        );

        item.setItemMeta(meta);
        return item;
    }
}