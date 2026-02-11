package com.moontegro.quests.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Profile {

    private UUID uuid;
    private List<Integer> completedQuests;
}
