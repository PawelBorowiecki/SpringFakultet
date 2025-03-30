package com.pawel.repositories;

import com.pawel.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    void deleteById(String id);
    Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel);
    void addVehicle(Vehicle vehicle);
}
