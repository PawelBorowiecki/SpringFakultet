package org.pawel.zad9.controller;

import org.pawel.zad9.dto.RentalRequest;
import org.pawel.zad9.model.Rental;
import org.pawel.zad9.model.User;
import org.pawel.zad9.service.RentalService;
import org.pawel.zad9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final UserService userService;
    private final RentalService rentalService;

    @Autowired
    public RentalController(UserService userService, RentalService rentalService) {
        this.userService = userService;
        this.rentalService = rentalService;
    }

    @GetMapping("/all")
    public List<Rental> getAllRentals(){
        return rentalService.findAll();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Rental> getActiveRentalByVehicleId(@PathVariable String vehicleId){
        return rentalService.findActiveRentalByVehicleId(vehicleId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        if(user.isPresent()){
            boolean rentalStatus = rentalService.rent(rentalRequest.getVehicleId(), user.get().getId());
            if(rentalStatus){
                Optional<Rental> rental = rentalService.findActiveRentalByVehicleId(rentalRequest.vehicleId);
                return ResponseEntity.status(HttpStatus.CREATED).body(rental.get());
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

//    @PutMapping("/return")
//    public ResponseEntity<Rental> returnRental(@RequestBody RentalRequest rentalRequest){
//        if(rentalRequest.vehicleId == null){
//            return ResponseEntity.badRequest().build();
//        }
//        try{
//            boolean returnStatus = rentalService.returnRental(rentalRequest.vehicleId);
//            if(returnStatus){
//                return ResponseEntity.ok().build();
//            }
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.notFound().build();
//    }
}
