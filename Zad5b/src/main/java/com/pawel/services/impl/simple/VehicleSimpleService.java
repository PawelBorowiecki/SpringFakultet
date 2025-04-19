package com.pawel.services.impl.simple;

import com.pawel.models.Rental;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.VehicleRepository;
import com.pawel.services.VehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleSimpleService implements VehicleService {
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    public VehicleSimpleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepo.findAll();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepo.findById(id);
    }

    @Override
    public Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice) {
        return vehicleRepo.findByPreferences(prefferedBrand, prefferedModel, prefferedPrice);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        List<Vehicle> allVehicles = vehicleRepo.findAll();
        for(Vehicle v : allVehicles){
            Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId());
            if(rental.isEmpty()){
                availableVehicles.add(v);
            }
        }
        return availableVehicles;
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if(rental.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void removeVehicle(String id) {
        vehicleRepo.deleteById(id);
    }
}
