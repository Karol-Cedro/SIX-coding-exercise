package com.kcedro.repository;

import com.kcedro.exceptions.MissionAlreadyExistsException;
import com.kcedro.model.Mission;

import java.util.*;

public class MissionRepository {
    private final Map<String, Mission> missions = new HashMap<>();

    public void addMission(Mission mission) {
        if (missions.containsKey(mission.getName())) {
            throw new MissionAlreadyExistsException("Mission with name " + mission.getName() + " already exists");
        }
        missions.put(mission.getName(), mission);
    }

    public Optional<Mission> getMission(String missionName) {
        return Optional.ofNullable(missions.get(missionName));
    }

    public List<Mission> getAllMissions() {
        return new ArrayList<>(missions.values());
    }
}
