package com.moontegro.quests.listener;

import com.moontegro.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final Quests plugin = Quests.getInstance();

    @EventHandler
    public void onAsync(AsyncPlayerPreLoginEvent event) {
        plugin.getProfileManager().load(event.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> this.plugin.getProfileManager()
                .save(event.getPlayer().getUniqueId()));
    }
}
