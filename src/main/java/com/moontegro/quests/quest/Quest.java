package com.moontegro.quests.quest;

import com.moontegro.quests.quest.type.QuestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class Quest<T> {

    private int id;
    private QuestType type;
    private T target;
    private int amount;
    private String rewardCommand;
    private List<ItemStack> rewardItems;

    private Material guiMaterial;
    private String guiItemName;
    private List<String> guiLore;
    private Set<ItemFlag> guiFlags;
    private int guiCustomModelData;
}