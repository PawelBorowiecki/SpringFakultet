package com.pawel.services;

import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserService {
    private UserRepository userRepository;
    private AuthService authService;
    private VehicleRepository vehicleRepository;
    private RentalRepository rentalRepository;

    public UserService(UserRepository userRepository, AuthService authService, VehicleRepository vehicleRepository, RentalRepository rentalRepository) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    public List<String> signUp(Scanner scanner){
        boolean registerStatus;
        System.out.println("Podaj login:");
        String inputLogin = scanner.next();
        System.out.println("Podaj haslo:");
        String inputPassword = scanner.next();
        System.out.println("Jezeli bedziesz administratorem wpisz A. Jesli nie, wpisz cokolwiek innego.");
        String inputRole = scanner.next();
        if(inputRole.equalsIgnoreCase("A")){
            registerStatus = authService.register(inputLogin, inputPassword, "ADMIN");
        }else{
            registerStatus = authService.register(inputLogin, inputPassword, "USER");
        }
        if(registerStatus){
            return List.of(inputLogin, inputPassword);
        }
        return List.of();
    }

    public Optional<User> signIn(Scanner scanner){
        System.out.println("Podaj login:");
        String inputLogin = scanner.next();
        System.out.println("Podaj haslo:");
        String inputPassword = scanner.next();
        if(authService.login(inputLogin, inputPassword).isPresent()){
            return authService.login(inputLogin, inputPassword);
        }else{
            System.out.println("Nie udalo sie zalogowac. Sprobuj ponownie w kolejnej operacji");
        }
        return Optional.empty();
    }

    public void showUserStatistics(User user){
        List<User> users = userRepository.findAll();
        List<Vehicle> fleet = vehicleRepository.findAll();
        List<Rental> rentals = rentalRepository.findAll();
        if(user.getRole().equals("ADMIN")){
            for(User u : users){
                System.out.println("Login: " + u.getLogin() + "\nPassword: " + u.getPassword() + "\nRole: " + u.getRole() + "\nRented vehicle: ");
                for(Vehicle v : fleet){
                    if(rentals.stream().filter(r -> r.getVehicleId().equals(v.getId()) && r.getUserId().equals(u.getId()) && r.getReturnDate() == null).findFirst().isPresent()){
                        System.out.println(v.toString());
                    }
                }
            }
        }else{
            System.out.println("Login: " + user.getLogin() + "\nPassword: " + user.getPassword() + "\nRole: " + user.getRole()+ "\nRented vehicle: ");
            for(Vehicle v : fleet){
                if(rentals.stream().filter(r -> r.getVehicleId().equals(v.getId()) && r.getUserId().equals(user.getId()) && r.getReturnDate() == null).findFirst().isPresent()){
                    System.out.println(v.toString());
                }
            }
        }
    }
}
