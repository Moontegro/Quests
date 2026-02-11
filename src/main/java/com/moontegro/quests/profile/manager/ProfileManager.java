package com.moontegro.quests.profile.manager;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.moontegro.quests.Quests;
import com.moontegro.quests.profile.Profile;
import lombok.Getter;
import org.bson.Document;

import java.util.*;

@Getter
public class ProfileManager {

    private final Quests plugin = Quests.getInstance();
    private final Map<UUID, Profile> profiles = new HashMap<>();

    public void load(UUID uuid) {
        Document document = plugin.getMongoManager().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();
        if (document != null) {
            List<Integer> completedQuests = document.getList("completedQuests", Integer.class);

            profiles.put(uuid, new Profile(uuid, completedQuests));
        } else {
            profiles.put(uuid, new Profile(uuid, new ArrayList<>()));
        }
    }

    public void save(UUID uuid) {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("completedQuests", profiles.get(uuid).getCompletedQuests());

        plugin.getMongoManager().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), document,
                new ReplaceOptions().upsert(true));
    }

    public void saveAll() {
        profiles.keySet().forEach(this::save);
    }
}
