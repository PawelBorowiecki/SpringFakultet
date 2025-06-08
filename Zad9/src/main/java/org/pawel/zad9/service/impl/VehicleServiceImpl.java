package org.pawel.zad9.service.impl;

import org.pawel.zad9.model.Vehicle;
import org.pawel.zad9.repository.RentalRepository;
import org.pawel.zad9.repository.VehicleRepository;
import org.pawel.zad9.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId() == null || vehicle.getId().isBlank()){
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepository.findByIsActiveTrueAndIdNotIn(rentalRepository.findRentedVehicleIds());
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findByIsActiveTrue();
        List<Vehicle> rentedVehicles = new ArrayList<>();
        for(Vehicle v : vehicles){
            if(rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isPresent()){
                rentedVehicles.add(v);
            }
        }
        return rentedVehicles;
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        List<Vehicle> vehicles = vehicleRepository.findByIsActiveTrue();
        for(Vehicle v : vehicles){
            if(rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(String id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if(vehicle.isPresent()){
            vehicle.get().setActive(false);
            vehicleRepository.deleteById(id);
            vehicleRepository.save(vehicle.get());
        }
    }
}
