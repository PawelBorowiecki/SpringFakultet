package com.pawel.services;

import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.VehicleRepository;

import java.time.LocalDateTime;
import java.util.*;

public class VehicleService {
    private VehicleRepository vehicleRepo;
    private RentalRepository rentalRepo;

    public VehicleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    public void addVehicle(Scanner scanner){
        Vehicle vehicle;
        System.out.println("Podaj marke pojazdu.");
        String inputBrand = scanner.next();
        System.out.println("Podaj model pojazdu.");
        String inputModel = scanner.next();
        System.out.println("Podaj rocznik pojazdu");
        int year = scanner.nextInt();
        System.out.println("Podaj nr rejstracyjny pojazdu.");
        String inputPlate = scanner.next();
        System.out.println("Podaj cene pojazdu.");
        double price = scanner.nextDouble();
        System.out.println("Podaj typ pojazdu.");
        String inputType = scanner.next();
        System.out.println("Czy chcesz dodac jakies konkretne atrybuty? Jesli tak, wybierz T, jesli nie wybierz cokolwiek innego");
        String isAttribute = scanner.next();
        if(isAttribute.equalsIgnoreCase("T")){
            Map<String, Object> attributes = new HashMap<>();
            System.out.println("Podaj ilosc atrybutow.");
            int attributesQuantity = scanner.nextInt();
            for(int i = 0; i < attributesQuantity; i++){
                System.out.println("Podaj nazwe " + (i+1) + ". atrybutu.");
                String inputAttributeName = scanner.next();
                System.out.println("Podaj wartosc " + (i+1) + ". atrybutu.");
                String inputAttributeValue = scanner.next();
                attributes.put(inputAttributeName, inputAttributeValue);
            }
            vehicle = Vehicle.builder()
                    .id(UUID.randomUUID().toString())
                    .brand(inputBrand)
                    .model(inputModel)
                    .year(year)
                    .price(price)
                    .plate(inputPlate)
                    .category(inputType)
                    .attributes(attributes)
                    .build();
        }else{
            vehicle = Vehicle.builder()
                    .id(UUID.randomUUID().toString())
                    .brand(inputBrand)
                    .model(inputModel)
                    .year(year)
                    .price(price)
                    .plate(inputPlate)
                    .category(inputType)
                    .build();
        }
        vehicleRepo.save(vehicle);
        System.out.println("Dodano pojazd o podanych parametrach\n" + vehicle.toString());
    }

    public void modifyVehicle(Scanner scanner){
        System.out.println("Podaj id pojazdu, ktory chcesz zmodyfikowac.");
        String vehicleId = scanner.next();
        Optional<Vehicle> vehicleFromTable = vehicleRepo.findById(vehicleId);
        if(vehicleFromTable.isPresent()){
            Vehicle vehicle;
            System.out.println("Podaj marke pojazdu.");
            String inputBrand = scanner.next();
            System.out.println("Podaj model pojazdu.");
            String inputModel = scanner.next();
            System.out.println("Podaj rocznik pojazdu");
            int year = scanner.nextInt();
            System.out.println("Podaj nr rejstracyjny pojazdu.");
            String inputPlate = scanner.next();
            System.out.println("Podaj cene pojazdu.");
            double price = scanner.nextDouble();
            System.out.println("Podaj typ pojazdu.");
            String inputType = scanner.next();
            System.out.println("Czy chcesz dodac jakies konkretne atrybuty? Jesli tak, wybierz T, jesli nie wybierz cokolwiek innego");
            String isAttribute = scanner.next();
            if(isAttribute.equalsIgnoreCase("T")){
                Map<String, Object> attributes = new HashMap<>();
                System.out.println("Podaj ilosc atrybutow.");
                int attributesQuantity = scanner.nextInt();
                for(int i = 0; i < attributesQuantity; i++){
                    System.out.println("Podaj nazwe " + (i+1) + ". atrybutu.");
                    String inputAttributeName = scanner.next();
                    System.out.println("Podaj wartosc " + (i+1) + ". atrybutu.");
                    String inputAttributeValue = scanner.next();
                    attributes.put(inputAttributeName, inputAttributeValue);
                }
                vehicle = Vehicle.builder()
                        .id(vehicleId)
                        .brand(inputBrand)
                        .model(inputModel)
                        .year(year)
                        .price(price)
                        .plate(inputPlate)
                        .category(inputType)
                        .attributes(attributes)
                        .build();
            }else{
                vehicle = Vehicle.builder()
                        .id(vehicleId)
                        .brand(inputBrand)
                        .model(inputModel)
                        .year(year)
                        .price(price)
                        .plate(inputPlate)
                        .category(inputType)
                        .build();
            }
            vehicleRepo.save(vehicle);
            System.out.println("Zmodyfikowano pojazd o podanych parametrach\n" + vehicle.toString());
        }else{
            System.out.println("Pojazd o podanym id nie istnieje, wiec zostanie dodany.");
            addVehicle(scanner);
        }
    }

    public void removeVehicle(Scanner scanner){
        System.out.println("Podaj id pojazdu, ktory chcesz usunac.");
        String vehicleId = scanner.next();
        List<Rental> rentals = rentalRepo.findAll();
        for(Rental r : rentals){
            if(r.getVehicleId().equals(vehicleId)){
                System.out.println("Nie mozna usunac pojazdu, poniewaz jest on wypozyczony.");
                return;
            }
        }
        vehicleRepo.deleteById(vehicleId);
        System.out.println("Usunieto pojazd.");
    }

    public void showAvailableVehicles(User user){
        List<Vehicle> fleet = vehicleRepo.findAll();
        if(user.getRole().equals("ADMIN")){
            for(Vehicle v : fleet){
                System.out.println(v.toString());
            }
        }else{
            List<Rental> rentals = rentalRepo.findAll();
            List<Vehicle> availableVehicles = new ArrayList<>();
            boolean isRented = false;
            for(Vehicle v : fleet){
                for(Rental r : rentals){
                    if(r.getVehicleId().equals(v.getId()) && r.getReturnDate() == null){
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

    public void returnVehicle(User user, Scanner scanner){
        System.out.println("Podaj id wypozyczenia pojazdu.");
        String rentalId = scanner.next();
        Optional<Rental> rental = rentalRepo.findById(rentalId);
        if(rental.isPresent() && user.getId().equals(rental.get().getUserId())){
            rental.get().setReturnDate(LocalDateTime.now().toString());
            rentalRepo.deleteById(rentalId);
            rentalRepo.save(rental.get());
            System.out.println("Zwrocono pojazd.");
        }else{
            System.out.println("Nie udalo sie zwrocic pojazdu.");
        }
    }
}
