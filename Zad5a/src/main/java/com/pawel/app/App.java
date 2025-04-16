package com.pawel.app;

import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.services.AuthService;
import com.pawel.services.RentalService;
import com.pawel.services.UserService;
import com.pawel.services.VehicleService;

import java.util.*;

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

    public void run(){
        User currentUser = null;
        Optional<User> optionalUser;
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
                        optionalUser = authService.login(inputLogin, inputPassword);
                        System.out.println("Zarejestrowano pomyslnie.");
                        if(optionalUser.isPresent()){
                            currentUser = optionalUser.get();
                            isLoggedIn = true;
                            System.out.println("Zalogowano.");
                        }else{
                            System.out.println("Nie udalo sie zalogowac.");
                        }
                    }else{
                        System.out.println("Uzytkownik o tym loginie juz istnieje.");
                    }
                }else{
                    System.out.println("Podaj login:");
                    String inputLogin = scanner.next();
                    System.out.println("Podaj haslo:");
                    String inputPassword = scanner.next();
                    optionalUser = authService.login(inputLogin, inputPassword);
                    if(optionalUser.isPresent()){
                        currentUser = optionalUser.get();
                        isLoggedIn = true;
                        System.out.println("Zalogowano.");
                    }else{
                        System.out.println("Nie udalo sie zalogowac.");
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
                        vehicleService.save(vehicle);
                    }else if(option == 2){
                        System.out.println("Podaj id pojazdu, ktory chcesz usunac.");
                        String vehicleId = scanner.next();
                        vehicleService.removeVehicle(vehicleId);
                    }else if(option == 3){
                        List<Vehicle> fleet = vehicleService.findAll();
                        for(Vehicle v : fleet){
                            System.out.println(v.toString());
                        }
                    }else if(option == 4){
                        userService.showStatistics(currentUser);
                    }else if(option == 5){
                        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedBrand = scanner.next();
                        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedModel = scanner.next();
                        System.out.println("Podaj jakiej ceny nie chcialbys przekroczyc.");
                        double prefferedPrice = scanner.nextDouble();
                        Optional<Vehicle> optionalVehicle = vehicleService.findByPreferences(prefferedBrand, prefferedModel, prefferedPrice);
                        if(optionalVehicle.isPresent()){
                            if(vehicleService.isAvailable(optionalVehicle.get().getId())){
                                rentalService.rent(optionalVehicle.get().getId(), currentUser.getId());
                                System.out.println("Wypozyczono pojazd o podanych parametrach:\n" + optionalVehicle.get().toString());
                            }else{
                                System.out.println("Wybrany pojazd nie jest dostepny.");
                            }
                        }else{
                            System.out.println("Nie udalo sie znalezc pojazdu spelniajacego Twoje wymagania.");
                        }
                    }else if(option == 6){
                        System.out.println("Podaj id pojazdu, ktory chcesz zwrocic.");
                        String vehicleId = scanner.next();
                        if(rentalService.isVehicleRented(vehicleId)){
                            rentalService.returnRental(vehicleId, currentUser.getId());
                            System.out.println("Zwrocono pojazd.");
                        }else{
                            System.out.println("Wybrany pojazd nie jest wypozyczony.");
                        }
                    }else if(option == 7){
                        isLoggedIn = false;
                    }else{
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
                        System.out.println("Podaj jakiej ceny nie chcialbys przekroczyc.");
                        double prefferedPrice = scanner.nextDouble();
                        Optional<Vehicle> optionalVehicle = vehicleService.findByPreferences(prefferedBrand, prefferedModel, prefferedPrice);
                        if(optionalVehicle.isPresent()){
                            if(vehicleService.isAvailable(optionalVehicle.get().getId())){
                                rentalService.rent(optionalVehicle.get().getId(), currentUser.getId());
                                System.out.println("Wypozyczono pojazd o podanych parametrach:\n" + optionalVehicle.get().toString());
                            }else{
                                System.out.println("Wybrany pojazd nie jest dostepny.");
                            }
                        }else{
                            System.out.println("Nie udalo sie znalezc pojazdu spelniajacego Twoje wymagania.");
                        }
                    }else if(option == 2){
                        System.out.println("Podaj id pojazdu, ktory chcesz zwrocic.");
                        String vehicleId = scanner.next();
                        if(rentalService.isVehicleRented(vehicleId)){
                            rentalService.returnRental(vehicleId, currentUser.getId());
                            System.out.println("Zwrocono pojazd.");
                        }else{
                            System.out.println("Wybrany pojazd nie jest wypozyczony.");
                        }
                    }else if(option == 3){
                        List<Vehicle> availableVehicles = vehicleService.findAvailableVehicles();
                        for(Vehicle v : availableVehicles){
                            System.out.println(v.toString());
                        }
                    }else if(option == 4){
                        userService.showStatistics(currentUser);
                    }else{
                        isLoggedIn = false;
                    }
                }
            }
        }
    }
}
