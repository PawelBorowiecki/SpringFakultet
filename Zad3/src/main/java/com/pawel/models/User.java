package com.pawel.models;

import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id, login, password, role;

    public boolean rentVehicle(String prefferedBrand, String prefferedModel, RentalRepository rentalRepository, VehicleRepository vehicleRepository){
        Optional<Vehicle> vehicle = vehicleRepository.findByPreferences(prefferedBrand, prefferedModel);
        if(vehicle.isPresent()){
            List<Rental> rentals = rentalRepository.findAll();
            for(Rental r : rentals){
                if(r.getUserId().equals(this.id) && r.getReturnDateTime().equals("NP")){
                    return false;
                }
            }
            Optional<Rental> optionalRental = rentalRepository.findById(vehicle.get().getId());
            if(!optionalRental.isPresent() || (optionalRental.isPresent() && optionalRental.get().getReturnDateTime().isEmpty())){
                Rental rental = new Rental();
                rental.setUserId(this.id);
                rental.setVehicleId(vehicle.get().getId());
                rental.setRentDateTime(LocalDateTime.now().toString());
                rental.setReturnDateTime("NP");
                rentalRepository.save(rental);
                return true;
            }
        }
        return false;
    }

    public void returnVehicle(String rentalId, RentalRepository rentalRepository){
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if(rental.isPresent() && this.id.equals(rental.get().getUserId())){
            rental.get().setReturnDateTime(LocalDateTime.now().toString());
            rentalRepository.deleteById(rentalId);
            rentalRepository.save(rental.get());
        }
    }

    public void addVehicle(Vehicle vehicle, VehicleRepository vehicleRepository){
        if(this.role.equals("ADMIN")){
            vehicleRepository.addVehicle(vehicle);
        }
    }

    public void removeVehicle(String vehicleId, VehicleRepository vehicleRepository, RentalRepository rentalRepository){
        if(this.role.equals("ADMIN")){
            List<Rental> rentals = rentalRepository.findAll();
            for(Rental r : rentals){
                if(r.getVehicleId().equals(vehicleId)){
                    return;
                }
            }
            vehicleRepository.deleteById(vehicleId);
        }
    }

    public void showAvailableVehicles(VehicleRepository vehicleRepository, RentalRepository rentalRepository){
        List<Vehicle> fleet = vehicleRepository.findAll();
        if(this.role.equals("ADMIN")){
            for(Vehicle v : fleet){
                System.out.println(v.toString());
            }
        }else{
            List<Rental> rentals = rentalRepository.findAll();
            List<Vehicle> availableVehicles = new ArrayList<>();
            boolean isRented = false;
            for(Vehicle v : fleet){
                for(Rental r : rentals){
                    if(r.getVehicleId().equals(v.getId()) && r.getReturnDateTime().equals("NP")){
                        isRented = true;
                        break;
                    }
                }
                if(!isRented){
                    availableVehicles.add(v);
                }else{
                    isRented = false;
                }
            }
            for(Vehicle v : availableVehicles){
                System.out.println(v.toString());
            }
        }
    }

    public void showUserStatistics(UserRepository userRepository, VehicleRepository vehicleRepository, RentalRepository rentalRepository){
        List<User> users = userRepository.findAll();
        List<Vehicle> fleet = vehicleRepository.findAll();
        List<Rental> rentals = rentalRepository.findAll();
        if(this.role.equals("ADMIN")){
            for(User u : users){
                System.out.println("Login: " + u.login + "\nPassword: " + u.password + "\nRole: " + u.role + "\nRented vehicle: ");
                for(Vehicle v : fleet){
                    if(rentals.stream().filter(r -> r.getVehicleId().equals(v.getId()) && r.getUserId().equals(u.id) && r.getReturnDateTime().equals("NP")).findFirst().isPresent()){
                        System.out.println(v.toString());
                    }
                }
            }
        }else{
            System.out.println("Login: " + this.login + "\nPassword: " + this.password + "\nRole: " + this.role + "\nRented vehicle: ");
            for(Vehicle v : fleet){
                if(!rentals.stream().filter(r -> r.getVehicleId().equals(v.getId()) && r.getUserId().equals(this.id) && r.getReturnDateTime().equals("NP")).findFirst().isEmpty()){
                    System.out.println(v.toString());
                }
            }
        }
    }
}
