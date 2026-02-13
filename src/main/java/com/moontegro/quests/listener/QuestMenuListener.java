package com.moontegro.quests.listener;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.utils.menu.holders.QuestInventoryHolder;
import com.moontegro.quests.utils.menu.type.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;

public class QuestMenuListener implements Listener {

    private final Quests plugin = Quests.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof QuestInventoryHolder)) return;
        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null
        || event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.getButton())) return;

        Player player = (Player) event.getWhoClicked();

        String buttonKey = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(plugin.getButton(), PersistentDataType.STRING);

        if (buttonKey.equalsIgnoreCase("next_page")) {
            this.plugin.getMenuManager().getMenus().get(MenuType.QUESTS).open(player, );
        } else if (buttonKey.equalsIgnoreCase("previous_page")) {
            this.plugin.getMenuManager().getMenus().
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
