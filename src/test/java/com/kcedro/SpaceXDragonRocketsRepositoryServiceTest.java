package com.kcedro;

import com.kcedro.exceptions.MissionAlreadyExistsException;
import com.kcedro.exceptions.MissionNotFoundException;
import com.kcedro.exceptions.RocketAlreadyAssignedException;
import com.kcedro.exceptions.RocketNotFoundException;
import com.kcedro.model.Mission;
import com.kcedro.model.MissionStatus;
import com.kcedro.model.Rocket;
import com.kcedro.model.RocketStatus;
import com.kcedro.repository.MissionRepository;
import com.kcedro.repository.RocketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Rocket newRocket = service.addNewRocket(rocketName);

        //then
        assertEquals(RocketStatus.ON_GROUND, newRocket.getStatus());
        assertEquals(rocketName, newRocket.getName());
    }

    @Test
    void assignRocketToMissionTest() {
        //given
        Mission mission = service.addNewMission("Mars");
        Rocket rocket = service.addNewRocket("Dragon");

        //when
        service.assignRocketToMission(rocket.getId(), mission.getName());

        //then
        assertTrue(mission.getAssignedRockets().contains(rocket.getId()));
        assertEquals(rocket.getAssignedMission(), mission.getName());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void assignRocketToMissionTwiceTest() {
        //given
        Mission mission1 = service.addNewMission("Mars");
        Mission mission2 = service.addNewMission("Luna");
        Rocket rocket = service.addNewRocket("Dragon");

        //when
        service.assignRocketToMission(rocket.getId(), mission1.getName());

        //then
        assertThrows(RocketAlreadyAssignedException.class,
                () -> service.assignRocketToMission(rocket.getId(), mission2.getName()));
    }

    @Test
    void missionAlreadyExistsTest() {
        //given
        String missionName = "Mars";

        //when
        service.addNewMission("Mars");

        //then
        assertThrows(MissionAlreadyExistsException.class,
                () -> service.addNewMission(missionName));
    }

    @Test
    void rocketNotFoundTest() {

        UUID rocketId = UUID.randomUUID();

        assertThrows(RocketNotFoundException.class,
                () -> service.changeRocketStatus(rocketId, RocketStatus.IN_SPACE));
    }

    @Test
    void missionNotFoundTest() {

        String missionName = "PolandIntoSpace";

        assertThrows(MissionNotFoundException.class,
                () -> service.changeMissionStatus(missionName, MissionStatus.ENDED));
    }

    @Test
    void changeRocketStatusTest() {
        //given
        Rocket rocket = service.addNewRocket("Dragon");
        RocketStatus newStatus = RocketStatus.IN_REPAIR;

        //when
        service.changeRocketStatus(rocket.getId(), newStatus);

        //then
        assertEquals(newStatus, rocket.getStatus());
    }

    @Test
    void updateMissionStatusAfterRocketStatusChangeTest() {
        //given
        Mission mission = service.addNewMission("Mars");
        Rocket rocket = service.addNewRocket("Dragon");
        service.assignRocketToMission(rocket.getId(), mission.getName());
        RocketStatus newStatus = RocketStatus.IN_REPAIR;

        //when
        service.changeRocketStatus(rocket.getId(), newStatus);

        //then
        assertEquals(MissionStatus.PENDING, mission.getStatus());
    }

    @Test
    void addNewMissionTest() {
        //given
        String missionName = "Mars";

        //when
        Mission newMission = service.addNewMission(missionName);

        //then
        assertEquals(MissionStatus.SCHEDULED, newMission.getStatus());
        assertEquals(missionName, newMission.getName());
    }

    @Test
    void assignMultipleRocketsToMissionTest() {
        //given
        Mission mission = service.addNewMission("Mars");
        List<UUID> rockets = List.of(
                service.addNewRocket("Dragon 1").getId(),
                service.addNewRocket("Dragon 2").getId(),
                service.addNewRocket("Dragon 3").getId()
        );

        //when
        service.assignRocketsToMission(rockets, mission.getName());

        //then
        assertEquals(mission.getAssignedRockets(), rockets);
        assertEquals(3, mission.getAssignedRockets().size());
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    void changeMissionStatusTest() {
        //given
        String missionName = "Mars";
        Mission mission = service.addNewMission(missionName);
        MissionStatus newMissionstatus = MissionStatus.IN_PROGRESS;

        //when
        service.changeMissionStatus(missionName, newMissionstatus);

        //then
        assertEquals(newMissionstatus, mission.getStatus());
    }

    @Test
    void rocketsShouldBeNoLongerAssignedToMissionAfterEnding() {
        //given
        String missionName = "Mars";
        Mission mission = service.addNewMission(missionName);
        Rocket rocket = service.addNewRocket("Dragon");
        service.assignRocketToMission(rocket.getId(), mission.getName());

        //when
        service.changeMissionStatus(missionName, MissionStatus.ENDED);

        //then
        assertEquals("", rocket.getAssignedMission());
        assertTrue(mission.getAssignedRockets().isEmpty());
    }

    @Test
    void missionsSummaryTest() {
        //given
        Mission mars = service.addNewMission("Mars");
        mars.setStatus(MissionStatus.SCHEDULED);
        Mission luna1 = service.addNewMission("Luna1");
        luna1.setStatus(MissionStatus.PENDING);
        Rocket dragon1 = service.addNewRocket("Dragon1");
        Rocket dragon2 = service.addNewRocket("Dragon2");
        service.assignRocketsToMission(List.of(dragon1.getId(), dragon2.getId()), luna1.getName());
        Mission doubleLanding = service.addNewMission("Double Landing");
        doubleLanding.setStatus(MissionStatus.ENDED);
        Mission transit = service.addNewMission("Transit");
        Rocket redDragon = service.addNewRocket("Red Dragon");
        Rocket dragonXL = service.addNewRocket("Dragon XL");
        dragonXL.setStatus(RocketStatus.IN_SPACE);
        Rocket falconHeavy = service.addNewRocket("Falcon Heavy");
        falconHeavy.setStatus(RocketStatus.IN_SPACE);
        service.assignRocketsToMission(
                List.of(redDragon.getId(), dragonXL.getId(), falconHeavy.getId()),
                transit.getName());
        Mission luna2 = service.addNewMission("Luna2");
        luna2.setStatus(MissionStatus.SCHEDULED);
        Mission verticalLanding = service.addNewMission("Vertical Landing");
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