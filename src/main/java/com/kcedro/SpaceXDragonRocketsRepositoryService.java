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
        Rocket rocket = rocketRepository.getRocket(rocketId).orElseThrow(
                () -> new IllegalArgumentException("No Rocket found with id " + rocketId)
        );
        rocket.setStatus(newStatus);
        Optional.ofNullable(rocket.getAssignedMission())
                .flatMap(missionRepository::getMission)
                .ifPresent(this::updateMissionStatus);
    }

    public void changeMissionStatus(String missionName, MissionStatus newStatus) {
        Mission mission = missionRepository.getMission(missionName).orElseThrow(
                () -> new IllegalArgumentException("No Mission found with name " + missionName)
        );

        if (newStatus == MissionStatus.ENDED && !mission.getAssignedRockets().isEmpty()) {
            mission.getAssignedRockets().stream()
                    .map(rocketRepository::getRocket)
                    .flatMap(Optional::stream)
                    .forEach(rocket -> rocket.assignMission(""));
            mission.getAssignedRockets().clear();
        }
        mission.setStatus(newStatus);
    }

    public void assignRocketToMission(UUID rocketId, String missionName) {
        Rocket rocket = rocketRepository.getRocket(rocketId).orElseThrow(
                () -> new IllegalArgumentException("No Rocket found with id " + rocketId)
        );
        Mission mission = missionRepository.getMission(missionName).orElseThrow(
                () -> new IllegalArgumentException("No Mission found with name " + missionName)
        );
        if (!rocket.getAssignedMission().isEmpty()) {
            throw new IllegalStateException("Rocket is already assigned to mission");
        }

        rocket.assignMission(missionName);
        rocket.setStatus(RocketStatus.IN_SPACE);
        mission.addRocket(rocketId);
        updateMissionStatus(mission);
    }

    public void assignRocketsToMission(List<UUID> listOfRockets, String missionName) {
        listOfRockets.forEach(rocketId -> assignRocketToMission(rocketId, missionName));
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