package org.pawel.zad9.service;

import org.pawel.zad9.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalService {
    boolean isVehicleRented(String vehicleId);
    Optional<Rental> findActiveRentalByVehicleId(String vehicleId);
    boolean rent(String vehicleId, String userId);
    boolean returnRental(String vehicleId);
    List<Rental> findAll();
}
