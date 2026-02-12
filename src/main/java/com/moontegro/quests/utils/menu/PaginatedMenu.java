package com.moontegro.quests.utils.menu;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.utils.color.Color;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public interface PaginatedMenu {

    String title();
    int size();
    Map<UUID, Integer> pages();

    default void open(Player player, int page) {
        pages().put(player.getUniqueId(), page);

        Inventory inventory = Bukkit.createInventory(player, size(), title());

        Map<Integer, ItemStack> fill = fillItems(player, page);
        fill.forEach(inventory::setItem);

        if (page > 0) {
            inventory.setItem(size() - 9, previousPage());
        }

        if ((page + 1) * slots().size() <
                Quests.getInstance().getQuestManager().getQuests().size()) {
            inventory.setItem(size() - 1, nextPage());
        }

        player.openInventory(inventory);
    }

    default void open(Player player) {
        open(player, 1);
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
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    default ItemStack previousPage() {
        ItemStack itemStack = new ItemStack(Material.valueOf(Quests.getInstance().getConfiguration().getConfiguration().getString("previous-page.item")));
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Color.translate(Quests.getInstance().getConfiguration().getConfiguration().getString("previous-page.name")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
