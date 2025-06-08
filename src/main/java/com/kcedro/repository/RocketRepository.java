package com.kcedro.repository;

import com.kcedro.model.Rocket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RocketRepository {
    private final Map<UUID, Rocket> rockets = new HashMap<>();

    public void add(Rocket rocket) {
        rockets.put(rocket.getId(), rocket);
    }

    public Rocket getRocket(UUID rocketId) {
        return rockets.get(rocketId);
    }
}
