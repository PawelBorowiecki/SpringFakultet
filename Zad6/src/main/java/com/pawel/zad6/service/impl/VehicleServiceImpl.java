package com.pawel.zad6.service.impl;

import com.pawel.zad6.model.Vehicle;
import com.pawel.zad6.repository.RentalRepository;
import com.pawel.zad6.repository.VehicleRepository;
import com.pawel.zad6.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        return this.vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return this.vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return this.vehicleRepository.findById(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId() == null || vehicle.getId().isBlank()){
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return this.vehicleRepository.findByIsActiveTrueAndIdNotIn(this.rentalRepository.findRentedVehicleIds());
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        List<Vehicle> vehicles = this.vehicleRepository.findByIsActiveTrue();
        List<Vehicle> rentedVehicles = new ArrayList<>();
        for(Vehicle v : vehicles){
            if(this.rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isPresent()){
                rentedVehicles.add(v);
            }
        }
        return rentedVehicles;
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        List<Vehicle> vehicles = this.vehicleRepository.findByIsActiveTrue();
        for(Vehicle v : vehicles){
            if(!this.rentalRepository.findByVehicleIdAndReturnDateIsNull(v.getId()).isPresent()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(String id) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findById(id);
        if(vehicle.isPresent()){
            vehicle.get().setActive(false);
            this.vehicleRepository.deleteById(id);
            this.vehicleRepository.save(vehicle.get());
        }
    }
}
