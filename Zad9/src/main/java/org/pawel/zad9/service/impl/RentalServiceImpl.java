package org.pawel.zad9.service.impl;

import org.pawel.zad9.model.Rental;
import org.pawel.zad9.model.User;
import org.pawel.zad9.model.Vehicle;
import org.pawel.zad9.repository.RentalRepository;
import org.pawel.zad9.repository.UserRepository;
import org.pawel.zad9.repository.VehicleRepository;
import org.pawel.zad9.service.RentalService;
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

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    @Transactional
    public boolean rent(String vehicleId, String userId) {
        if(!isVehicleRented(vehicleId)){
            Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
            Optional<User> user = userRepository.findById(userId);
            if(vehicle.isPresent() && user.isPresent()){
                Rental newRental = Rental.builder()
                        .id(UUID.randomUUID().toString())
                        .vehicle(vehicle.get())
                        .user(user.get())
                        .rentDate(LocalDateTime.now())
                        .returnDate(null)
                        .build();

                rentalRepository.save(newRental);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean returnRental(String vehicleId) {
        List<Rental> rentals = rentalRepository.findAll();
        Optional<Rental> rental = rentals.stream().filter(r -> r.getVehicle().getId().equals(vehicleId)).findFirst();
        if(rental.isPresent()){
            rentalRepository.save(rental.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
