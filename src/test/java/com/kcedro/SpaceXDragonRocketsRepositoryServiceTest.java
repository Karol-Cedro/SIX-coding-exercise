package com.kcedro;

import com.kcedro.model.Mission;
import com.kcedro.model.MissionStatus;
import com.kcedro.model.Rocket;
import com.kcedro.model.RocketStatus;
import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpaceXDragonRocketsRepositoryServiceTest {

    private RocketRepository rocketRepository;
    private MissionRepository missionRepository;
    private SpaceXDragonRocketsRepositoryService service;

    @BeforeEach
    void setUp() {
        rocketRepository = new RocketRepository();
        missionRepository = new MissionRepository();
        service = new SpaceXDragonRocketsRepositoryService(rocketRepository, missionRepository);
    }

    @Test
    void addNewRocketTest() {
        //given
        String rocketName = "Dragon";

        //when
        Rocket newRocket = service.addRocket(rocketName);

        //then
        assertEquals(RocketStatus.ON_GROUND, newRocket.getStatus());
        assertEquals(rocketName, newRocket.getName());
    }

    @Test
    void assignRocketToMissionTest() {
        //given
        Mission mission = service.addMission("Mars");
        Rocket rocket = service.addRocket("Dragon");

        //when
        service.assignRocketToMission(rocket.getId(), mission.getName());

        //then
        assertTrue(mission.getAssignedRockets().contains(rocket.getId()));
        assertEquals(rocket.getAssignedMission(), mission.getName());
    }

    @Test
    void changeRocketStatusTest() {
        //given
        Rocket rocket = service.addRocket("Dragon");
        RocketStatus newStatus = RocketStatus.IN_SPACE;

        //when
        service.changeRocketStatus(rocket.getId(), newStatus);

        //then
        assertEquals(newStatus, rocket.getStatus());
    }

    @Test
    void addNewMissionTest() {
        //given
        String missionName = "Mars";

        //when
        Mission newMission = service.addMission(missionName);

        //then
        assertEquals(MissionStatus.SCHEDULED, newMission.getStatus());
        assertEquals(missionName, newMission.getName());
    }

    @Test
    void assignMultipleRocketsToMissionTest() {
        //given
        Mission mission = new Mission();
        List<Rocket> rockets = new ArrayList<>();

        //when
        service.assignRocketsToMission(mission, rockets);

        //then
        assertEquals(service.getMission(mission).getRockets().size() == rockets.size());
    }

    @Test
    void changeMissionStatusTest() {
        //given
        String missionName = "Mars";
        Mission mission = service.addMission(missionName);
        MissionStatus newMissionstatus = MissionStatus.IN_PROGRESS;

        //when
        service.changeMissionStatus(missionName, newMissionstatus);

        //then
        assertEquals(newMissionstatus, mission.getStatus());
    }

    @Test
    void missionsSummaryTest() {
        //given
        String expectedSummary = "";

        //when
        String summary = service.getSummary();

        //then
        assertEquals(expectedSummary, summary);
    }


}