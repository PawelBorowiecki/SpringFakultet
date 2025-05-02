package com.pawel.zad6.controller;

import com.pawel.zad6.model.Vehicle;
import com.pawel.zad6.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/all")
    public List<Vehicle> getAllVehicles(){
        return this.vehicleService.findAll();
    }
    @GetMapping("/allActive")
    public List<Vehicle> getAllActiveVehicles(){
        return this.vehicleService.findAllActive();
    }

    @GetMapping("/allAvailable")
    public List<Vehicle> getAvailableVehicles(){
        return this.vehicleService.findAvailableVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id){
        return this.vehicleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/allRented")
    public List<Vehicle> getAllRentedVehicles(){
        return this.vehicleService.findRentedVehicles();
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle){
        try{
            Vehicle savedVehicle = this.vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable String id){
        this.vehicleService.deleteById(id);
    }
}