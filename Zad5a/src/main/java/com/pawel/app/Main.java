package com.pawel.app;

import com.pawel.repositories.impl.RentalHibernateRepository;
import com.pawel.repositories.impl.UserHibernateRepository;
import com.pawel.repositories.impl.VehicleHibernateRepository;
import com.pawel.services.impl.AuthHibernateService;
import com.pawel.services.impl.RentalHibernateService;
import com.pawel.services.impl.UserHibernateService;
import com.pawel.services.impl.VehicleHibernateService;

public class Main {
    public static void main(String[] args) {
        UserHibernateRepository userRepo = new UserHibernateRepository();
        VehicleHibernateRepository vehicleRepo = new VehicleHibernateRepository();
        RentalHibernateRepository rentalRepo = new RentalHibernateRepository();

        AuthHibernateService authService = new AuthHibernateService(userRepo);
        VehicleHibernateService vehicleService = new VehicleHibernateService(vehicleRepo, rentalRepo);
        RentalHibernateService rentalService = new RentalHibernateService(rentalRepo, vehicleRepo, userRepo);
        UserHibernateService userService = new UserHibernateService(userRepo, vehicleRepo, rentalRepo);

        App app = new App(authService, userService, vehicleService, rentalService);
        app.run();
    }
}