package com.kcedro.repository;

import com.kcedro.model.Rocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RocketRepository {
    private final Map<UUID, Rocket> rockets = new HashMap<>();

    public void add(Rocket rocket) {
        rockets.put(rocket.getId(), rocket);
    }

    public Optional<Rocket> getRocket(UUID rocketId) {
        return Optional.ofNullable(rockets.get(rocketId));
    }
}
