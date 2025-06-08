package com.kcedro;

import com.kcedro.model.Rocket;
import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;

public class SpaceXDragonRocketsRepositoryService {
    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    public SpaceXDragonRocketsRepositoryService(RocketRepository rocketRepository, MissionRepository missionRepository) {
        this.rocketRepository = rocketRepository;
        this.missionRepository = missionRepository;
    }

    public Rocket addRocket(String name) {
        Rocket rocket = new Rocket(name);
        rocketRepository.add(rocket);
        return rocket;
    }
}