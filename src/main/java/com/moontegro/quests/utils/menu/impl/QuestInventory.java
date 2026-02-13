package com.moontegro.quests.utils.menu.impl;

import com.moontegro.quests.Quests;
import com.moontegro.quests.utils.color.Color;
import com.moontegro.quests.utils.menu.PaginatedMenu;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestInventory implements PaginatedMenu {

    private final Quests plugin = Quests.getInstance();

    @Override
    public Component title() {
        return Color.translate(plugin.getConfiguration().getConfiguration().getString("quest-inventory.title"));
    }

    @Override
    public int size() {
        return plugin.getConfiguration().getConfiguration().getInt("quest-inventory.size");
    }

    @Override
    public Map<UUID, Integer> pages() {
        return new HashMap<>();
    }

    @Override
    public void open(Player player) {
        PaginatedMenu.super.open(player);
    }
}
