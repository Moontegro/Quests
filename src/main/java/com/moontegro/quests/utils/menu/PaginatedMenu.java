package com.moontegro.quests.utils.menu;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.utils.color.Color;
import com.moontegro.quests.utils.menu.holders.QuestInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
        int currentPage = pages().getOrDefault(player.getUniqueId(), 0);
        int nextPage = currentPage + 1;

        if (!canFill(nextPage) && currentPage != 0) {
            return;
        }

        pages().put(player.getUniqueId(), nextPage);

        Inventory inventory = Bukkit.createInventory(
                new QuestInventoryHolder(),
                size(),
                title()
        );

        Map<Integer, ItemStack> fill = fillItems(player, nextPage);
        fill.forEach(inventory::setItem);

        if (nextPage > 0) {
            inventory.setItem(size() - 9, previousPage());
        }

        if (canFill(nextPage + 1)) {
            inventory.setItem(size() - 1, nextPage());
        }

        player.openInventory(inventory);
    }

    default boolean canFill(int page) {
        int minimumItems = page * 9;

        return Quests.getInstance()
                .getQuestManager()
                .getQuests()
                .size() > minimumItems;
    }

    default Map<Integer, ItemStack> fillItems(Player player, int page) {
        List<Quest<?>> quests = new ArrayList<>(
                Quests.getInstance().getQuestManager().getQuests().values()
        );

        List<Integer> slots = slots();
        Map<Integer, ItemStack> inventorySlots = new HashMap<>();

        int start = page * slots.size();
        int end = Math.min(start + slots.size(), quests.size());

        for (int i = start; i < end; i++) {
            Quest<?> quest = quests.get(i);
            int slot = slots.get(i - start);

            ItemStack itemStack = new ItemStack(quest.getGuiMaterial());
            ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                meta.displayName(Color.translate(quest.getGuiItemName()));
                meta.lore(new ArrayList<>());
                itemStack.setItemMeta(meta);
            }

            inventorySlots.put(slot, itemStack);
        }

        return inventorySlots;
    }

    default List<Integer> slots() {
        return Quests.getInstance().getConfiguration().getConfiguration().getIntegerList("quest-inventory.available-slots");
    }

    default ItemStack nextPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(Quests.getInstance().getConfiguration().getConfiguration().getString("next-page.item")));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Color.translate(Quests.getInstance().getConfiguration().getConfiguration().getString("next-page.name")));
        meta.getPersistentDataContainer().set(Quests.getInstance().getButton(), PersistentDataType.STRING, "next_page");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    default ItemStack previousPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(Quests.getInstance().getConfiguration().getConfiguration().getString("previous-page.item")));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Color.translate(Quests.getInstance().getConfiguration().getConfiguration().getString("previous-page.name")));
        meta.getPersistentDataContainer().set(Quests.getInstance().getButton(), PersistentDataType.STRING, "previous_page");
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
