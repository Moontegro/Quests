package com.moontegro.quests.command;

import com.moontegro.quests.Quests;
import com.moontegro.quests.utils.color.Color;
import com.moontegro.quests.utils.menu.PaginatedMenu;
import com.moontegro.quests.utils.menu.type.MenuType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestCommand implements CommandExecutor {

    private final Quests plugin = Quests.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Color.translate("&cYou must be a player to execute this command."));
            return false;
        }

        PaginatedMenu questMenu = plugin.getMenuManager().getMenus().get(MenuType.QUESTS);
        questMenu.open(player);

        return true;
    }
}
