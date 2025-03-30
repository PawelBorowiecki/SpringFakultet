package com.pawel.repository;

import com.pawel.vehicles.Vehicle;

import java.util.List;

public interface IVehicleRepository {
    String rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category);
    String returnVehicle(int vin);
    List<Vehicle> getVehicles();
    Vehicle getVehicle(int carId);
    void save();
    void addVehicle(Vehicle vehicle);
    boolean removeVehicle(int vin);
}
