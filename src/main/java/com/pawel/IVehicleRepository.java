package com.pawel;

import java.util.List;

public interface IVehicleRepository {
    String rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category);
    String returnVehicle(int vin);
    List<Vehicle> getVehicles();
    void save();
    void addVehicle(Vehicle vehicle);
    void removeVehicle(int vin);
}
