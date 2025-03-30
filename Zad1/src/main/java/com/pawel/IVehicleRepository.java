package com.pawel;

import java.util.List;

public interface IVehicleRepository {
    List<Vehicle> vehicles = List.of(
            new Car("Volvo", "XC90", 2022, 45625, 20000, false),
            new Car("Volvo", "S60", 2018, 47649, 22000, false),
            new Car("BMW", "X6", 2024, 73625, 25000, false),
            new Car("BMW", "MX3", 2023, 79856, 35000, false),
            new Car("Mercedes", "S-klasse", 2021, 62543, 26000, false),
            new Motorcycle("Harley-Davidson", "Breakout", 2021, 956546, 10000, false, "A"),
            new Motorcycle("YAMAHA", "MT", 2017, 87654, 9001, false, "AM")
    );

    String rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category);
    String returnVehicle(int vin);
    List<Vehicle> getVehicles();
    void save(List<Vehicle> vehicles);
}
