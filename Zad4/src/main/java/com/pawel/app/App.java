package com.pawel.app;

import com.pawel.models.User;
import com.pawel.services.AuthService;
import com.pawel.services.RentalService;
import com.pawel.services.UserService;
import com.pawel.services.VehicleService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private final AuthService authService;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final RentalService rentalService;
    private final Scanner scanner = new Scanner(System.in);

    public App(AuthService authService, UserService userService, VehicleService vehicleService, RentalService rentalService) {
        this.authService = authService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.rentalService = rentalService;
    }

    public void run() {
        User currentUser = null;
        String loginType;
        int option;
        boolean isLoggedIn = false;
        System.out.println("Witaj w wypozyczalni pojazdow!");
        while(true){
            if(!isLoggedIn){
                do{
                    System.out.println("Zaloguj sie lub zarejestruj. Jesli chcesz sie zalogowac wybierz L, jesli chcesz sie zarejestrować wybierz S.");
                    loginType = scanner.next();
                }while(!(loginType.equalsIgnoreCase("L") || loginType.equalsIgnoreCase("S")));
                if(loginType.equalsIgnoreCase("S")){
                    List<String> loginDetails = userService.signUp(scanner);
                    if(!loginDetails.isEmpty()){
                        currentUser = authService.login(loginDetails.get(0), loginDetails.get(1)).get();
                        isLoggedIn = true;
                    }
                }else{
                    Optional<User> randomUser = userService.signIn(scanner);
                    if(randomUser.isPresent()){
                        currentUser = randomUser.get();
                        isLoggedIn = true;
                    }
                }
            }else{
                if(currentUser.getRole().equals("ADMIN")){
                    System.out.println("""
                        Jesli chcesz dodac pojazd wybierz 1.
                        Jesli chcesz usunac pojazd wybierz 2.
                        Jesli chcesz zmodyfikowac pojazd wybierz 3.
                        Jesli chcesz zobaczyc flote pojazdow jakimi dysponujemy wybierz 4.
                        Jesli chcesz wyswietlic statystyki uzytkownikow wybierz 5.
                        Jesli chcesz wypozyczyc pojazd wybierz 6.
                        Jesli chcesz zwrocic pojazd wybierz 7.
                        Jesli chcesz sie wylogowac wybierz 8.
                        Jesli chcesz zakonczyc dzialanie serwisu wybierz inny numer.""");
                    option = scanner.nextInt();
                    if(option == 1){
                        vehicleService.addVehicle(scanner);
                    }else if(option == 2){
                        vehicleService.removeVehicle(scanner);
                    }else if(option == 3){
                        vehicleService.modifyVehicle(scanner);
                    }else if(option == 4){
                        vehicleService.showAvailableVehicles(currentUser);
                    }else if(option == 5){
                        userService.showUserStatistics(currentUser);
                    }else if(option == 6){
                        rentalService.rentVehicle(currentUser, scanner);
                    }else if(option == 7){
                        vehicleService.returnVehicle(currentUser, scanner);
                    }else if(option == 8){
                        isLoggedIn = false;
                    }else{
                        return;
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
                        rentalService.rentVehicle(currentUser, scanner);
                    }else if(option == 2){
                        vehicleService.returnVehicle(currentUser, scanner);
                    }else if(option == 3){
                        vehicleService.showAvailableVehicles(currentUser);
                    }else if(option == 4){
                        userService.showUserStatistics(currentUser);
                    }else{
                        isLoggedIn = false;
                    }
                }
            }
        }
    }
}
