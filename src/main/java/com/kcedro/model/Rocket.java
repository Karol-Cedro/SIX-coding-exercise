package com.kcedro.model;

import java.util.UUID;

public class Rocket {

    private final UUID id;
    private final String name;
    private RocketStatus status;
    private String assignedMission;


    public Rocket(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.status = RocketStatus.ON_GROUND;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public RocketStatus getStatus() {
        return status;
    }

    public String getAssignedMission() {
        return assignedMission;
    }

    public void assignMission(String assignedMission) {
        this.assignedMission = assignedMission;
    }

    public void setStatus(RocketStatus status) {
        this.status = status;
    }
}
