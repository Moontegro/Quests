package com.moontegro.quests.utils.menu.manager;

import com.moontegro.quests.utils.menu.PaginatedMenu;
import com.moontegro.quests.utils.menu.impl.QuestInventory;
import com.moontegro.quests.utils.menu.type.MenuType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MenuManager {

    private final Map<MenuType, PaginatedMenu> menus = new HashMap<>();

    public MenuManager() {
        menus.put(MenuType.QUESTS, new QuestInventory());
    }
}
