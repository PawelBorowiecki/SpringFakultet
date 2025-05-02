package com.pawel.zad6.controller;

import com.pawel.zad6.dto.RentalRequest;
import com.pawel.zad6.model.Rental;
import com.pawel.zad6.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/all")
    public List<Rental> getAllRentals(){
        return this.rentalService.findAll();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Rental> getActiveRentalByVehicleId(@PathVariable String vehicleId){
        return this.rentalService.findActiveRentalByVehicleId(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest){
        if(rentalRequest.vehicleId == null || rentalRequest.userId == null){
            return ResponseEntity.badRequest().build();
        }
        try{
            Rental rental = this.rentalService.rent(rentalRequest.vehicleId, rentalRequest.userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(rental);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/return")
    public ResponseEntity<Rental> returnRental(@RequestBody RentalRequest rentalRequest){
        if(rentalRequest.vehicleId == null || rentalRequest.userId == null){
            return ResponseEntity.badRequest().build();
        }
        try{
            boolean returnStatus = this.rentalService.returnRental(rentalRequest.vehicleId, rentalRequest.userId);
            if(returnStatus){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
