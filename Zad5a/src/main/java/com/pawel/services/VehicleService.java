package com.pawel.services;

import com.pawel.models.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> findAll();
    Optional<Vehicle> findById(String id);
    Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice);
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAvailableVehicles();
    boolean isAvailable(String vehicleId);
    void removeVehicle(String id);
}