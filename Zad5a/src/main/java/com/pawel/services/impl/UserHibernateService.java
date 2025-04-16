package com.pawel.services.impl;

import com.pawel.db.HibernateConfig;
import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.impl.RentalHibernateRepository;
import com.pawel.repositories.impl.UserHibernateRepository;
import com.pawel.repositories.impl.VehicleHibernateRepository;
import com.pawel.services.UserService;
import org.hibernate.Session;

import java.util.List;

public class UserHibernateService implements UserService {
    private final UserHibernateRepository userRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;

    public UserHibernateService(UserHibernateRepository userRepo, VehicleHibernateRepository vehicleRepo, RentalHibernateRepository rentalRepo) {
        this.userRepo = userRepo;
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public void showStatistics(User user) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            userRepo.setSession(session);
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);

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
}
