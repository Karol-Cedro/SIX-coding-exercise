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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
        Mission mission = service.addMission("Mars");
        List<UUID> rockets = List.of(
                service.addRocket("Dragon 1").getId(),
                service.addRocket("Dragon 2").getId(),
                service.addRocket("Dragon 3").getId()
        );

        //when
        service.assignRocketsToMission(rockets, mission.getName());

        //then
        assertEquals(mission.getAssignedRockets(), rockets);
        assertEquals(3, mission.getAssignedRockets().size());
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
        Mission mars = service.addMission("Mars");
        mars.setStatus(MissionStatus.SCHEDULED);
        Mission luna1 = service.addMission("Luna1");
        luna1.setStatus(MissionStatus.PENDING);
        Rocket dragon1 = service.addRocket("Dragon1");
        Rocket dragon2 = service.addRocket("Dragon2");
        service.assignRocketsToMission(List.of(dragon1.getId(), dragon2.getId()), luna1.getName());
        Mission doubleLanding = service.addMission("Double Landing");
        doubleLanding.setStatus(MissionStatus.ENDED);
        Mission transit = service.addMission("Transit");
        Rocket redDragon = service.addRocket("Red Dragon");
        Rocket dragonXL = service.addRocket("Dragon XL");
        dragonXL.setStatus(RocketStatus.IN_SPACE);
        Rocket falconHeavy = service.addRocket("Falcon Heavy");
        falconHeavy.setStatus(RocketStatus.IN_SPACE);
        service.assignRocketsToMission(
                List.of(redDragon.getId(), dragonXL.getId(), falconHeavy.getId()),
                transit.getName());
        Mission luna2 = service.addMission("Luna2");
        luna2.setStatus(MissionStatus.SCHEDULED);
        Mission verticalLanding = service.addMission("Vertical Landing");
        verticalLanding.setStatus(MissionStatus.ENDED);

        //when
        List<Mission> summary = service.getMissionsSummary();

        //then
        assertEquals(summary.getFirst().getName(), transit.getName());
        assertEquals(summary.get(1).getName(), luna1.getName());
        assertEquals(summary.get(2).getName(), verticalLanding.getName());
        assertEquals(summary.get(3).getName(), mars.getName());
        assertEquals(summary.get(4).getName(), luna2.getName());
        assertEquals(summary.get(5).getName(), doubleLanding.getName());
    }


}