package com.kcedro;

import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;

public class SpaceXDragonRocketsRepositoryService {
    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    public SpaceXDragonRocketsRepositoryService(RocketRepository rocketRepository, MissionRepository missionRepository) {
        this.rocketRepository = rocketRepository;
        this.missionRepository = missionRepository;
    }
}