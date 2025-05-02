package com.pawel.zad6.service.impl;

import com.pawel.zad6.model.Rental;
import com.pawel.zad6.model.User;
import com.pawel.zad6.model.Vehicle;
import com.pawel.zad6.repository.RentalRepository;
import com.pawel.zad6.repository.UserRepository;
import com.pawel.zad6.repository.VehicleRepository;
import com.pawel.zad6.service.RentalService;
import com.pawel.zad6.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository, VehicleRepository vehicleRepository, UserRepository userRepository, VehicleService vehicleService) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.vehicleService = vehicleService;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return this.rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    @Transactional
    public Rental rent(String vehicleId, String userId) {
        if(!this.vehicleService.isAvailable(vehicleId)){
            throw new IllegalStateException("Vehicle " + vehicleId + " is not available for rent.");
        }
        Vehicle vehicle = this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle consistency error. ID: " + vehicleId));
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> {
                    return new EntityNotFoundException("User not found with ID: " + userId);
                });
        Rental newRental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(LocalDateTime.now())
                .returnDate(null)
                .build();

        return this.rentalRepository.save(newRental);
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        List<Rental> rentals = this.rentalRepository.findAll();
        Optional<Rental> rental = rentals.stream().filter(r -> r.getVehicle().getId().equals(vehicleId)).findFirst();
        if(rental.isPresent()){
            this.rentalRepository.deleteById(rental.get().getId());
            rental.get().setReturnDate(LocalDateTime.now());
            this.rentalRepository.save(rental.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Rental> findAll() {
        return this.rentalRepository.findAll();
    }
}
