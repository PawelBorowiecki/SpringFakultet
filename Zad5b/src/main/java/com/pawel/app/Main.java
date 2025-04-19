package com.pawel.app;

import com.pawel.repositories.RentalRepository;
import com.pawel.repositories.UserRepository;
import com.pawel.repositories.VehicleRepository;
import com.pawel.repositories.impl.hibernate.RentalHibernateRepository;
import com.pawel.repositories.impl.hibernate.UserHibernateRepository;
import com.pawel.repositories.impl.hibernate.VehicleHibernateRepository;
import com.pawel.repositories.impl.jdbc.RentalJdbcRepository;
import com.pawel.repositories.impl.jdbc.UserJdbcRepository;
import com.pawel.repositories.impl.jdbc.VehicleJdbcRepository;
import com.pawel.repositories.impl.json.RentalJsonRepository;
import com.pawel.repositories.impl.json.UserJsonRepository;
import com.pawel.repositories.impl.json.VehicleJsonRepository;
import com.pawel.services.impl.hibernate.AuthHibernateService;
import com.pawel.services.impl.hibernate.RentalHibernateService;
import com.pawel.services.impl.hibernate.UserHibernateService;
import com.pawel.services.impl.hibernate.VehicleHibernateService;
import com.pawel.services.impl.simple.AuthSimpleService;
import com.pawel.services.impl.simple.RentalSimpleService;
import com.pawel.services.impl.simple.UserSimpleService;
import com.pawel.services.impl.simple.VehicleSimpleService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String storageType;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("""
                Wybierz gdzie chcesz przechowywac dane.
                Jesli chcesz przechowywac je w Hibernacie wybierz H.
                Jesli chcesz przechowywac je w plikach JSON wybierz J.
                Jesli chcesz  przechowywac je w bazie danych wybierz D.""");
            storageType = scanner.next();
        }while(!storageType.equalsIgnoreCase("J") && !storageType.equalsIgnoreCase("D") && !storageType.equalsIgnoreCase("H"));

        if(storageType.equalsIgnoreCase("H")){
            UserHibernateRepository userRepo = new UserHibernateRepository();
            VehicleHibernateRepository vehicleRepo = new VehicleHibernateRepository();
            RentalHibernateRepository rentalRepo = new RentalHibernateRepository();

            AuthHibernateService authService = new AuthHibernateService(userRepo);
            VehicleHibernateService vehicleService = new VehicleHibernateService(vehicleRepo, rentalRepo);
            RentalHibernateService rentalService = new RentalHibernateService(rentalRepo, vehicleRepo, userRepo);
            UserHibernateService userService = new UserHibernateService(userRepo, vehicleRepo, rentalRepo);

            App app = new App(authService, userService, vehicleService, rentalService);
            app.run();
        }else{
            UserRepository userRepo;
            VehicleRepository vehicleRepo;
            RentalRepository rentalRepo;
            if(storageType.equalsIgnoreCase("J")){
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }else{
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository(vehicleRepo, userRepo);
            }
            AuthSimpleService authService = new AuthSimpleService(userRepo);
            VehicleSimpleService vehicleService = new VehicleSimpleService(vehicleRepo, rentalRepo);
            RentalSimpleService rentalService = new RentalSimpleService(rentalRepo, vehicleRepo, userRepo);
            UserSimpleService userService = new UserSimpleService(userRepo, vehicleRepo, rentalRepo);

            App app = new App(authService, userService, vehicleService, rentalService);
            app.run();
        }
    }
}