package com.kcedro;

import com.kcedro.model.Mission;
import com.kcedro.model.MissionStatus;
import com.kcedro.model.Rocket;
import com.kcedro.model.RocketStatus;
import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpaceXDragonRocketsRepositoryService {
    private final RocketRepository rocketRepository;
    private final MissionRepository missionRepository;

    public SpaceXDragonRocketsRepositoryService(RocketRepository rocketRepository, MissionRepository missionRepository) {
        this.rocketRepository = rocketRepository;
        this.missionRepository = missionRepository;
    }

    public Rocket addNewRocket(String name) {
        Rocket rocket = new Rocket(name);
        rocketRepository.add(rocket);
        return rocket;
    }

    public Mission addNewMission(String name) {
        Mission mission = new Mission(name);
        missionRepository.addMission(mission);
        return mission;
    }

    public void changeRocketStatus(UUID rocketId, RocketStatus newStatus) {
        Optional<Rocket> rocket = rocketRepository.getRocket(rocketId);

        if (rocket.isPresent()) {
            rocket.get().setStatus(newStatus);
            missionRepository.getMission(rocket.get().getAssignedMission()).ifPresent(this::updateMissionStatus);
        } else {
            System.out.println("No Rocket found with id " + rocketId);
        }
    }

    public void changeMissionStatus(String missionName, MissionStatus newStatus) {
        Optional<Mission> mission = missionRepository.getMission(missionName);

        if (mission.isPresent()) {

            if (newStatus == MissionStatus.ENDED) {
                mission.get().getAssignedRockets().stream()
                        .map(rocketRepository::getRocket)
                        .flatMap(Optional::stream)
                        .forEach(rocket -> rocket.assignMission(null));
                mission.get().getAssignedRockets().clear();
            }
            mission.get().setStatus(newStatus);
        } else {
            System.out.println("No Mission found with name " + missionName);
        }
    }

    public void assignRocketToMission(UUID rocketId, String missionName) {
        Optional<Rocket> rocket = rocketRepository.getRocket(rocketId);
        if (rocket.isEmpty()) {
            System.out.println("No Rocket found with id " + rocketId);
            return;
        }
        Optional<Mission> mission = missionRepository.getMission(missionName);
        if (mission.isEmpty()) {
            System.out.println("No Mission found with name " + missionName);
            return;
        }
        rocket.get().assignMission(missionName);
        rocket.get().setStatus(RocketStatus.IN_SPACE);
        mission.get().addRocket(rocketId);
        updateMissionStatus(mission.get());
    }

    public void assignRocketsToMission(List<UUID> listOfRockets, String missionName) {
        Optional<Mission> mission = missionRepository.getMission(missionName);
        if (mission.isEmpty()) {
            System.out.println("No Mission found with name " + missionName);
            return;
        }
        listOfRockets.forEach(rocketId -> {
            rocketRepository.getRocket(rocketId).ifPresent(rocket -> {
                mission.get().addRocket(rocketId);
            });
        });
        updateMissionStatus(mission.get());
    }

    public List<Mission> getMissionsSummary() {
        return missionRepository.getAllMissions().stream().sorted(
                Comparator.comparing(
                                (Mission m) -> m.getAssignedRockets().size())
                        .thenComparing(Mission::getName)
                        .reversed()).toList();
    }

    private void updateMissionStatus(Mission mission) {
        boolean hasRocketInRepair = mission.getAssignedRockets().stream()
                .map(rocketRepository::getRocket)
                .flatMap(Optional::stream)
                .anyMatch(rocket -> rocket.getStatus() == RocketStatus.IN_REPAIR);
        mission.setStatus(hasRocketInRepair ? MissionStatus.PENDING : MissionStatus.IN_PROGRESS);
    }
}