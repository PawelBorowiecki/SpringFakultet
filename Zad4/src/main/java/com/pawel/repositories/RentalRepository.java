package com.pawel.repositories;

import com.pawel.models.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    List<Rental> findAll();
    Optional<Rental> findById(String id);
    Rental save(Rental rental);
    void deleteById(String id);
    Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId);
}
