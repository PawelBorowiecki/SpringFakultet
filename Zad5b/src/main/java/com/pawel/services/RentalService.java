package com.pawel.services;

import com.pawel.models.Rental;

import java.util.List;

public interface RentalService {
    boolean isVehicleRented(String vehicleId);
    Rental rent(String vehicleId, String userId);
    boolean returnRental(String vehicleId, String userId);
    List<Rental> findAll();
}
