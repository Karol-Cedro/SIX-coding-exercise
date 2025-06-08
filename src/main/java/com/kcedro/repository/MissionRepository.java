package com.kcedro.repository;

import com.kcedro.model.Mission;

import java.util.HashMap;
import java.util.Map;

public class MissionRepository {
    private final Map<String, Mission> missions = new HashMap<>();

    public void addMission(Mission mission) {
        missions.put(mission.getName(), mission);
    }

    public Mission getMission(String missionName) {
        return missions.get(missionName);
    }
}
