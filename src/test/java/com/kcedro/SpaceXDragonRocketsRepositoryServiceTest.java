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
        Rocket rocket = new Rocket();

        //when
        service.addRocket(rocket);

        //then
        assertEquals(rocket.getStatus(), RocketStatus.ON_GROUND);
    }

    @Test
    void assignRocketToMissionTest() {
        //given
        Mission mission = new Mission();
        Rocket rocket = new Rocket();

        //when
        service.assignRocketsToMission(mission, rocket);

        //then
        assertTrue(service.getMission(mission).getRockets().contains(rocket));

    }

    @Test
    void changeRocketStatusTest() {
        //given
        Rocket rocket = new Rocket();
        RocketStatus newStatus = RocketStatus.IN_SPACE;

        //when
        service.changeRocketStatus(rocket, newStatus);

        //then
        assertTrue(service.getRocket(rocket).getStatus().equals(newStatus));

    }

    @Test
    void addNewMissionTest() {
        //given
        Mission mission = new Mission();

        //when
        service.addMission();

        //then
        assertEquals(mission.getStatus(), MissionStatus.SCHEDULED);
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
        Mission mission = new Mission();
        MissionStatus newMissionstatus = MissionStatus.IN_PROGRESS;

        //when
        service.changeMissionStatus(mission, newMissionstatus);

        //then
        assertEquals(service.getMission(mission).getStatus().equals(newMissionstatus));
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