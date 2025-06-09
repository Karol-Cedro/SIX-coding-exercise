package com.kcedro;

import com.kcedro.model.Mission;
import com.kcedro.model.MissionStatus;
import com.kcedro.model.Rocket;
import com.kcedro.model.RocketStatus;
import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;

import java.util.Optional;
import java.util.UUID;

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

    public Mission addMission(String name) {
        Mission mission = new Mission(name);
        missionRepository.addMission(mission);
        return mission;
    }

    public void changeRocketStatus(UUID rocketId, RocketStatus newStatus) {
        Optional<Rocket> rocket = rocketRepository.getRocket(rocketId);

        if (rocket.isPresent()) {
            rocket.get().setStatus(newStatus);
        }else{
            System.out.println("No Rocket found with id " + rocketId);
        }
    }

    public void changeMissionStatus(String missionName, MissionStatus newStatus) {
        Optional<Mission> mission = missionRepository.getMission(missionName);

        if (mission.isPresent()) {
            mission.get().setStatus(newStatus);
        } else {
            System.out.println("No Mission found with name " + missionName);
        }
    }
}