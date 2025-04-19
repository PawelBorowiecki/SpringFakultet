package com.pawel.services.impl.simple;

import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;
import com.pawel.services.RentalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalSimpleService implements RentalService {
    private final RentalRepository rentalRepo;
    private final VehicleRepository vehicleRepo;
    private final UserRepository userRepo;

    public RentalSimpleService(RentalRepository rentalRepo, VehicleRepository vehicleRepo, UserRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        if(rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()){
            throw new IllegalStateException("Vehicle is rented");
        }

        Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found."));
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));

        Rental rental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(LocalDateTime.now().toString())
                .build();

        rentalRepo.save(rental);
        return rental;
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Optional<Rental> rentalFromDb = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if(rentalFromDb.isEmpty()){
            throw new IllegalStateException("Vehicle is not rented");
        }

        rentalFromDb.get().setReturnDate(LocalDateTime.now().toString());
        rentalRepo.save(rentalFromDb.get());
        return true;
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepo.findAll();
    }
}
