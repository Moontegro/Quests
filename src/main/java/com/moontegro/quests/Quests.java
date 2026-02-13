package com.moontegro.quests;

import com.moontegro.quests.command.QuestCommand;
import com.moontegro.quests.mongo.MongoManager;
import com.moontegro.quests.profile.manager.ProfileManager;
import com.moontegro.quests.quest.manager.QuestManager;
import com.moontegro.quests.utils.config.Config;
import com.moontegro.quests.utils.menu.manager.MenuManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class Quests extends JavaPlugin {

    @Getter
    private static Quests instance;

    private Config configuration, quests;

    private MongoManager mongoManager;
    private QuestManager questManager;
    private ProfileManager profileManager;
    private MenuManager menuManager;

    private final NamespacedKey button = new NamespacedKey(this, "button");

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfiguration();
        this.loadCommands();
        this.loadListeners(Bukkit.getPluginManager());

        this.mongoManager = new MongoManager();
        this.questManager = new QuestManager();
        this.profileManager = new ProfileManager();
        this.menuManager = new MenuManager();

        this.questManager.load();
    }

    @Override
    public void onDisable() {
        this.profileManager.saveAll();

        instance = null;
    }

    private void loadConfiguration() {
        this.configuration = new Config(this, new File(getDataFolder(), "configuration.yml"),
                new YamlConfiguration(), "configuration.yml");
        this.quests = new Config(this, new File(getDataFolder(), "quests.yml"),
                new YamlConfiguration(), "quests.yml");

        this.configuration.create();
        this.quests.create();
    }

    private void loadListeners(PluginManager pluginManager) {

    }

    private void loadCommands() {
        getCommand("quest").setExecutor(new QuestCommand());
    }
}
