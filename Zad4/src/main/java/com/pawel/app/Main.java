package com.pawel.app;

import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;
import com.pawel.repositories.impl.jdbc.RentalJdbcRepository;
import com.pawel.repositories.impl.jdbc.UserJdbcRepository;
import com.pawel.repositories.impl.jdbc.VehicleJdbcRepository;
import com.pawel.repositories.impl.json.RentalJsonRepository;
import com.pawel.repositories.impl.json.UserJsonRepository;
import com.pawel.repositories.impl.json.VehicleJsonRepository;
import com.pawel.services.AuthService;
import com.pawel.services.RentalService;
import com.pawel.services.UserService;
import com.pawel.services.VehicleService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String storageType;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("""
                Wybierz gdzie chcesz przechowywac dane.
                Jesli chcesz przechowywac je w plikach JSON wybierz J.
                Jesli chcesz  przechowywac je w bazie danych wybierz D.""");
            storageType = scanner.next();
        }while(!storageType.equalsIgnoreCase("J") && !storageType.equalsIgnoreCase("D"));

        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu DONE
        //TODO: Utworzenie RentalJdbcRepository implementujacej RentalRepository DONE
        //TODO: Utworzenie UserJdbcRepository implementujacej UserRepository DONE

        //TODO: Dorzucenie do projektu swoich jsonrepo. DONE

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "D" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "J" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów DONE
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow DONE
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo, vehicleRepo);
        UserService userService = new UserService(userRepo, authService, vehicleRepo, rentalRepo);

        //TODO:Przerzucenie logiki interakcji z userem do App DONE
        App app = new App(authService, userService, vehicleService, rentalService);
        app.run();

    }
}