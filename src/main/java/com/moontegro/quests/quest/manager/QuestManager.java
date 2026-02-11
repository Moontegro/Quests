package com.moontegro.quests.quest.manager;

import com.moontegro.quests.Quests;
import com.moontegro.quests.quest.Quest;
import com.moontegro.quests.quest.type.QuestType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;

@Getter
public class QuestManager {

    private final Quests plugin = Quests.getInstance();
    private final Map<Integer, Quest<?>> quests = new HashMap<>();

    private final Map<QuestType, Function<Object, ?>> targetConverters = Map.of(
            QuestType.KILLING, target -> {
                if (target instanceof String s) return EntityType.valueOf(s);
                if (target instanceof Integer i) return EntityType.values()[i];
                return target;
            },
            QuestType.MINING, target -> {
                if (target instanceof String s) return Material.valueOf(s);
                if (target instanceof Integer i) return Material.values()[i];
                return target;
            },
            QuestType.EXPLORING, Object::toString
    );

    public void load() {
        for (final String s : plugin.getQuests().getConfiguration().getConfigurationSection("quests").getKeys(false)) {
            int id = plugin.getQuests().getConfiguration().getInt("quests." + s + ".id");
            QuestType type = QuestType.valueOf(plugin.getQuests().getConfiguration().getString("quests." + s + ".type"));
            Object target = plugin.getQuests().getConfiguration().get("quests." + s + ".target");
            int requiredAmount = plugin.getQuests().getConfiguration().getInt("quests." + s + ".required-amount");
            String command = plugin.getQuests().getConfiguration().getString("quests." + s + ".reward-command");

            List<ItemStack> itemStacks = new ArrayList<>();
            plugin.getQuests().getConfiguration().getStringList("quests." + s + ".reward-items")
                    .forEach(i -> itemStacks.add(new ItemStack(Material.valueOf(i))));

            Material material = Material.valueOf(plugin.getQuests().getConfiguration().getString("quests." + s + ".gui-item.material"));
            String name = plugin.getQuests().getConfiguration().getString("quests." + s + ".gui-item.name");
            List<String> lore = new ArrayList<>(plugin.getQuests().getConfiguration().getStringList("quests." + s + ".gui-item.lore"));
            Set<ItemFlag> itemFlags = new HashSet<>();
            plugin.getQuests().getConfiguration().getStringList("quests." + s + ".gui-item.flags").forEach(f -> itemFlags.add(ItemFlag.valueOf(f)));
            int customModelData = plugin.getQuests().getConfiguration().getInt("quests." + s + ".gui-item.custom-model-data");

            createQuestByType(id, type, target, requiredAmount, command, itemStacks, material, name, lore, itemFlags, customModelData);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void createQuestByType(int id, QuestType type, Object target, int requiredAmount, String command,
                                       List<ItemStack> items, Material guiMaterial, String guiName,
                                       List<String> guiLore, Set<ItemFlag> guiFlags, int guiCustomModelData) {

        Function<Object, ?> converter = targetConverters.get(type);
        if (converter == null) return;

        T convertedTarget;
        try {
            convertedTarget = (T) converter.apply(target);
        } catch (Exception e) {
            return;
        }

        Quest<T> quest = new Quest<>(id, type, convertedTarget, requiredAmount, command,
                items, guiMaterial, guiName, guiLore, guiFlags, guiCustomModelData);

        quests.put(id, quest);
    }
}