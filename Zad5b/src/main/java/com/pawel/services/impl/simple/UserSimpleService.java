package com.pawel.services.impl.simple;

import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;
import com.pawel.services.UserService;

import java.util.List;

public class UserSimpleService implements UserService {
    private final UserRepository userRepo;
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    public UserSimpleService(UserRepository userRepo, VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public void showStatistics(User user) {
        List<User> users = userRepo.findAll();
        List<Vehicle> fleet = vehicleRepo.findAll();
        List<Rental> rentals = rentalRepo.findAll();
        if(user.getRole().equals("ADMIN")){
            for(User u : users){
                System.out.println("Login: " + u.getLogin() + "\nPassword: " + u.getPassword() + "\nRole: " + u.getRole() + "\nRented vehicle: ");
                for(Vehicle v : fleet){
                    if(rentals.stream().filter(r -> r.getVehicle().getId().equals(v.getId()) && r.getUser().getId().equals(u.getId()) && r.getReturnDate() == null).findFirst().isPresent()){
                        System.out.println(v.toString());
                    }
                }
            }
        }else{
            System.out.println("Login: " + user.getLogin() + "\nPassword: " + user.getPassword() + "\nRole: " + user.getRole()+ "\nRented vehicle: ");
            for(Vehicle v : fleet){
                if(rentals.stream().filter(r -> r.getVehicle().getId().equals(v.getId()) && r.getUser().getId().equals(user.getId()) && r.getReturnDate() == null).findFirst().isPresent()){
                    System.out.println(v.toString());
                }
            }
        }
    }
}
