package com.moontegro.quests.listener;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.utils.menu.PaginatedMenu;
import com.moontegro.quests.utils.menu.holders.QuestInventoryHolder;
import com.moontegro.quests.utils.menu.type.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class QuestMenuListener implements Listener {

    private final Quests plugin = Quests.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof QuestInventoryHolder)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();

        if (!meta.getPersistentDataContainer().has(plugin.getButton(), PersistentDataType.STRING)) return;

        Player player = (Player) event.getWhoClicked();

        String buttonKey = meta.getPersistentDataContainer()
                .get(plugin.getButton(), PersistentDataType.STRING);

        if (buttonKey == null) return;

        PaginatedMenu menu = this.plugin.getMenuManager()
                .getMenus()
                .get(MenuType.QUESTS);

        int currentPage = menu.pages().getOrDefault(player.getUniqueId(), 1);

        if (buttonKey.equalsIgnoreCase("next_page")) {
            menu.pages().put(player.getUniqueId(), currentPage + 1);
            menu.open(player);
        }

        else if (buttonKey.equalsIgnoreCase("previous_page")) {
            int newPage = Math.max(1, currentPage - 1);
            menu.pages().put(player.getUniqueId(), newPage);
            menu.open(player);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
            this.plugin.getMenuManager().getMenus().get(MenuType.QUESTS).pages().remove(player.getUniqueId());
        }
    }
}
