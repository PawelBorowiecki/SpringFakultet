package com.pawel.services;

import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RentalService {
    private RentalRepository rentalRepo;
    private VehicleRepository vehicleRepo;

    public RentalService(RentalRepository rentalRepo, VehicleRepository vehicleRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
    }

    public void rentVehicle(User user, Scanner scanner){
        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
        String prefferedBrand = scanner.next();
        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
        String prefferedModel = scanner.next();
        System.out.println("Podaj jakiej ceny nie chcialbys przekroczyc.");
        double prefferedPrice = scanner.nextDouble();
        Optional<Vehicle> vehicle = vehicleRepo.findByPreferences(prefferedBrand, prefferedModel, prefferedPrice);

        if(vehicle.isPresent()){
            List<Rental> rentals = rentalRepo.findAll();
            for(Rental r : rentals){
                if(r.getUserId().equals(user.getId()) && r.getReturnDate() == null){    //TODO nwm czy z tym nullem zadziala
                    System.out.println("Nie mamy pojazdu spelniajcego Twoje wymagania, badz wybrany pojazd jest wypozyczony.");
                    return;
                }
            }
            Optional<Rental> optionalRental = rentalRepo.findById(vehicle.get().getId());
            if(!optionalRental.isPresent() || (optionalRental.isPresent() && optionalRental.get().getReturnDate().isEmpty())){  //TODO nwm czy to empty jest ok
                Rental rental = Rental.builder()
                        .userId(user.getId())
                        .vehicleId(vehicle.get().getId())
                        .rentDate(LocalDateTime.now().toString())
                        .returnDate(null)
                        .build();
                rentalRepo.save(rental);
                System.out.println("Wypozyczono pojazd.");
            }
        }
    }
}
