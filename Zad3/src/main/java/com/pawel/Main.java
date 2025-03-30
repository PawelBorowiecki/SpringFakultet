package com.pawel;

import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.impl.RentalJsonRepository;
import com.pawel.repositories.impl.UserJsonRepository;
import com.pawel.repositories.impl.VehicleJsonRepository;
import com.pawel.services.AuthService;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        UserJsonRepository userRepository = new UserJsonRepository();
        VehicleJsonRepository vehicleRepository = new VehicleJsonRepository();
        RentalJsonRepository rentalRepository = new RentalJsonRepository();
        AuthService authService = new AuthService();
        Scanner scanner = new Scanner(System.in);
        int option, year, attributesQuantity;
        String loginType, inputLogin, inputPassword, inputRole, inputBrand, inputModel, inputType, inputPlate, isAttribute, inputAttributeName, inputAttributeValue, rentalId;
        boolean isLoggedIn = false;
        User currentUser = null;

        System.out.println("Witaj w wypozyczalni pojazdow!");
        while(true) {
            if(!isLoggedIn){
                do{
                    System.out.println("Zaloguj sie lub zarejestruj. Jesli chcesz sie zalogowac wybierz L, jesli chcesz sie zarejestrować wybierz S.");
                    loginType = scanner.next();
                }while(!(loginType.equalsIgnoreCase("L") || loginType.equalsIgnoreCase("S")));
                if(loginType.equalsIgnoreCase("S")){
                    currentUser = new User();
                    System.out.println("Podaj login:");
                    inputLogin = scanner.next();
                    System.out.println("Podaj haslo:");
                    inputPassword = scanner.next();
                    System.out.println("Jezeli bedziesz administratorem wpisz A. Jesli nie, wpisz cokolwiek innego.");
                    inputRole = scanner.next();
                    if(inputRole.equalsIgnoreCase("A")){
                        isLoggedIn = authService.register(inputLogin, inputPassword, "ADMIN", userRepository, currentUser);
                    }else{
                        isLoggedIn = authService.register(inputLogin, inputPassword, "USER", userRepository, currentUser);
                    }
                    if(!isLoggedIn){
                        System.out.println("Nie udalo sie zarejestrowac. Sprobuj ponownie w kolejnej operacji");
                    }
                }else{
                    System.out.println("Podaj login:");
                    inputLogin = scanner.next();
                    System.out.println("Podaj haslo:");
                    inputPassword = scanner.next();
                    if(authService.login(inputLogin, inputPassword, userRepository).isPresent()){
                        currentUser = authService.login(inputLogin, inputPassword, userRepository).get();
                        isLoggedIn = true;
                    }else{
                        System.out.println("Nie udalo sie zalogowac. Sprobuj ponownie w kolejnej operacji");
                    }
                }
            }else{
                if(currentUser.getRole().equals("ADMIN")){
                    System.out.println("""
                        Jesli chcesz dodac pojazd wybierz 1.
                        Jesli chcesz usunac pojazd wybierz 2.
                        Jesli chcesz zobaczyc flote pojazdow jakimi dysponujemy wybierz 3.
                        Jesli chcesz wyswietlic statystyki uzytkownikow wybierz 4.
                        Jesli chcesz wypozyczyc pojazd wybierz 5.
                        Jesli chcesz zwrocic pojazd wybierz 6.
                        Jesli chcesz sie wylogowac wybierz 7.
                        Jesli chcesz zakonczyc dzialanie serwisu wybierz inny numer.""");
                    option = scanner.nextInt();
                    if(option == 1){
                        Vehicle vehicle;
                        System.out.println("Podaj marke pojazdu.");
                        inputBrand = scanner.next();
                        System.out.println("Podaj model pojazdu.");
                        inputModel = scanner.next();
                        System.out.println("Podaj rocznik pojazdu");
                        year = scanner.nextInt();
                        System.out.println("Podaj nr rejstracyjny pojazdu.");
                        inputPlate = scanner.next();
                        System.out.println("Podaj typ pojazdu.");
                        inputType = scanner.next();
                        System.out.println("Czy chcesz dodac jakies konkretne atrybuty? Jesli tak, wybierz T, jesli nie wybierz cokolwiek innego");
                        isAttribute = scanner.next();
                        if(isAttribute.equalsIgnoreCase("T")){
                            Map<String, Object> attributes = new HashMap<>();
                            System.out.println("Podaj ilosc atrybutow.");
                            attributesQuantity = scanner.nextInt();
                            for(int i = 0; i < attributesQuantity; i++){
                                System.out.println("Podaj nazwe " + (i+1) + ". atrybutu.");
                                inputAttributeName = scanner.next();
                                System.out.println("Podaj wartosc " + (i+1) + ". atrybutu.");
                                inputAttributeValue = scanner.next();
                                attributes.put(inputAttributeName, inputAttributeValue);
                            }
                            vehicle= Vehicle.builder()
                                    .id(UUID.randomUUID().toString() + LocalTime.now().toString())
                                    .brand(inputBrand)
                                    .model(inputModel)
                                    .year(year)
                                    .plate(inputPlate)
                                    .category(inputType)
                                    .attributes(attributes)
                                    .build();
                        }else{
                            vehicle = Vehicle.builder()
                                    .id(UUID.randomUUID().toString() + LocalTime.now().toString())
                                    .brand(inputBrand)
                                    .model(inputModel)
                                    .year(year)
                                    .plate(inputPlate)
                                    .category(inputType)
                                    .build();
                        }
                        currentUser.addVehicle(vehicle, vehicleRepository);
                        System.out.println("Dodano pojazdu o nastepujacych parametrach:\n" + vehicle.toString());
                    }else if(option == 2){
                        System.out.println("Podaj id pojazdu, ktory chcesz usunac.");
                        inputType = scanner.next();
                        currentUser.removeVehicle(inputType, vehicleRepository, rentalRepository);
                    }else if(option == 3){
                        currentUser.showAvailableVehicles(vehicleRepository, rentalRepository);
                    }else if(option == 4){
                        currentUser.showUserStatistics(userRepository, vehicleRepository, rentalRepository);
                    }else if(option == 5){
                        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedBrand = scanner.next();
                        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedModel = scanner.next();
                        boolean rentStatus = currentUser.rentVehicle(prefferedBrand, prefferedModel, rentalRepository, vehicleRepository);
                        if(rentStatus){
                            System.out.println("Wypozyczono pojazd.");
                        }else{
                            System.out.println("Nie udalo sie wypozyczyc pojazdu");
                        }
                    }else if(option == 6){
                        System.out.println("Podaj id wypozyczenia pojazdu.");
                        rentalId = scanner.next();
                        currentUser.returnVehicle(rentalId, rentalRepository);
                    }else if(option == 7){
                        isLoggedIn = false;
                        System.out.println("Wylogowano");
                    }else{
                        System.out.println("Dziekujemy za skorzystanie z naszych uslug.");
                        break;
                    }
                }else{
                    System.out.println("""
                            Jesli chcesz wypozyczyc pojazd wybierz 1.
                            Jesli chcesz zwrocic pojazd wybierz 2.
                            Jesli chcesz wyswietlic dostepne pojazdy wybierz 3.
                            Jesli chcesz wyswietlic statystyki swojego konta wybierz 4.
                            Jesli chcesz sie wylogowac wybierz inna liczbe.""");
                    option = scanner.nextInt();
                    if(option == 1){
                        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedBrand = scanner.next();
                        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedModel = scanner.next();
                        boolean rentStatus = currentUser.rentVehicle(prefferedBrand, prefferedModel, rentalRepository, vehicleRepository);
                        if(rentStatus){
                            System.out.println("Wypozyczono pojazd.");
                        }else{
                            System.out.println("Nie udalo sie wypozyczyc pojazdu");
                        }
                    }else if(option == 2){
                        System.out.println("Podaj id wypozyczenia pojazdu.");
                        rentalId = scanner.next();
                        currentUser.returnVehicle(rentalId, rentalRepository);
                    }else if(option == 3){
                        currentUser.showAvailableVehicles(vehicleRepository, rentalRepository);
                    }else if(option == 4){
                        currentUser.showUserStatistics(userRepository, vehicleRepository, rentalRepository);
                    }else{
                        isLoggedIn = false;
                        System.out.println("Wylogowano");
                    }
                }
            }
        }
    }
}