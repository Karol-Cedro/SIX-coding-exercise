package com.kcedro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mission {

    private final String name;
    private MissionStatus status;
    private List<UUID> assignedRockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.assignedRockets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public List<UUID> getAssignedRockets() {
        return assignedRockets;
    }

    public void addRocket(UUID rocketId) {
        assignedRockets.add(rocketId);
    }
}
