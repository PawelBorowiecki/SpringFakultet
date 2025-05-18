package com.pawel.zad7.controller;

import com.pawel.zad7.dto.RentalRequest;
import com.pawel.zad7.model.Rental;
import com.pawel.zad7.model.User;
import com.pawel.zad7.repository.UserRepository;
import com.pawel.zad7.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
    private final RentalService rentalService;
    private final UserRepository userRepository;

    @Autowired
    public RentalController(RentalService rentalService, UserRepository userRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<Rental> getAllRentals(@AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return rentalService.findAll();
        }
        return List.of();
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Rental> getActiveRentalByVehicleId(@PathVariable String vehicleId, @AuthenticationPrincipal UserDetails userDetails){
        if(userDetails.getAuthorities().stream().filter(a -> a.getAuthority().equals("ROLE_ADMIN")).findFirst().isPresent()){
            return rentalService.findActiveRentalByVehicleId(vehicleId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/rent")
    public ResponseEntity<Rental> rentVehicle(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User not found" + login));
        Rental rental = rentalService.rent(rentalRequest.getVehicleId(), user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PutMapping("/return")
    public ResponseEntity<Rental> returnRental(@RequestBody RentalRequest rentalRequest, @AuthenticationPrincipal UserDetails userDetails){
        if(rentalRequest.vehicleId == null){
            return ResponseEntity.badRequest().build();
        }
        try{
            Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
            if(user.isPresent()){
                boolean returnStatus = rentalService.returnRental(rentalRequest.vehicleId, user.get().getId());
                if(returnStatus){
                    return ResponseEntity.ok().build();
                }
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.notFound().build();
    }
}
