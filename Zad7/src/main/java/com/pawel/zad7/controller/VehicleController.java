package com.pawel.zad7.controller;

import com.pawel.zad7.model.Vehicle;
import com.pawel.zad7.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public List<Vehicle> getAllVehicles(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return vehicleService.findAll();
        }
        for(var a : userDetails.getAuthorities()){
            System.out.println(a);
        }

        return List.of();
    }
    @GetMapping("/allActive")
    public List<Vehicle> getAllActiveVehicles(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return vehicleService.findAllActive();
        }
        return List.of();
    }

    @GetMapping("/allAvailable")
    public List<Vehicle> getAvailableVehicles(){
        return vehicleService.findAvailableVehicles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return vehicleService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/allRented")
    public List<Vehicle> getAllRentedVehicles(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return vehicleService.findRentedVehicles();
        }
        return List.of();
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            try{
                Vehicle savedVehicle = this.vehicleService.save(vehicle);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            this.vehicleService.deleteById(id);
        }
    }
}
