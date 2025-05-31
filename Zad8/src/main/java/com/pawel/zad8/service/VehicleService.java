package com.pawel.zad8.service;

import com.pawel.zad8.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> findAll();
    List<Vehicle> findAllActive();
    Optional<Vehicle> findById(String id);
    Vehicle save(Vehicle vehicle);
    List<Vehicle> findAvailableVehicles();
    List<Vehicle> findRentedVehicles();
    boolean isAvailable(String vehicleId);
    void deleteById(String id);
}
