package com.moontegro.quests.profile;

import com.moontegro.quests.quest.PlayerQuest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Profile {

    private UUID uuid;
    private List<Integer> completedQuests;
    private Set<PlayerQuest> onGoingQuests;
}
