package com.pawel;

import com.pawel.userutils.Role;
import com.pawel.userutils.User;
import com.pawel.vehicles.Car;
import com.pawel.vehicles.Motorcycle;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option, carYear, carVin, carPrice;
        String loginType, category, inputLogin, inputPassword, inputRole, inputBrand, inputModel, inputType;
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
                    System.out.println("Podaj login:");
                    inputLogin = scanner.next();
                    System.out.println("Podaj haslo:");
                    inputPassword = scanner.next();
                    inputPassword = DigestUtils.sha256Hex(inputPassword);
                    System.out.println("Jezeli bedziesz administratorem wpisz A. Jesli nie, wpisz cokolwiek innego.");
                    inputRole = scanner.next();
                    if(inputRole.equalsIgnoreCase("A")){
                        currentUser = new User(inputLogin, inputPassword, Role.ADMIN);
                    }else{
                        currentUser = new User(inputLogin, inputPassword, Role.NORMAL);
                    }
                    currentUser.register();
                    isLoggedIn = true;
                }else{
                    System.out.println("Podaj login:");
                    inputLogin = scanner.next();
                    System.out.println("Podaj haslo:");
                    inputPassword = scanner.next();
                    inputPassword = DigestUtils.sha256Hex(inputPassword);
                    System.out.println("Jezeli masz uprawnienia administratora wpisz A. Jesli nie, wpisz cokolwiek innego.");
                    inputRole = scanner.next();
                    if(inputRole.equalsIgnoreCase("A")){
                        currentUser = new User(inputLogin, inputPassword, Role.ADMIN);
                    }else{
                        currentUser = new User(inputLogin, inputPassword, Role.NORMAL);
                    }
                    isLoggedIn = currentUser.signIn();
                }
            }else{
                if(currentUser.getRole().name().equals("ADMIN")){
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
                        System.out.println("Podaj marke pojazdu.");
                        inputBrand = scanner.next();
                        System.out.println("Podaj model pojazdu.");
                        inputModel = scanner.next();
                        System.out.println("Podaj rocznik pojazdu.");
                        carYear = scanner.nextInt();
                        do{
                            System.out.println("Podaj pieciocyfrowy nr VIN pojazdu.");
                            carVin = scanner.nextInt();
                        }while(carVin < 10000 || carVin > 99999);
                        System.out.println("Podaj cene wynajecia pojazdu.");
                        carPrice = scanner.nextInt();
                        do{
                            System.out.println("Podaj typ pojazdu. Jesli to samochod wybierz C, jesli to motocykl wybierz M.");
                            inputType = scanner.next();
                        }while(!(inputType.equalsIgnoreCase("C") || inputType.equalsIgnoreCase("M")));
                        System.out.println("Podaj kategorie potrzebna do kierowania tym pojazdem.");
                        category = scanner.next();
                        if(inputType.equalsIgnoreCase("C")){
                            currentUser.addVehicle(new Car(inputBrand, inputModel, carYear, carVin, carPrice, false, category.toUpperCase()));
                        }else{
                            currentUser.addVehicle(new Motorcycle(inputBrand, inputModel, carYear, carVin, carPrice, false, category.toUpperCase()));
                        }
                    }else if(option == 2){
                        System.out.println("Podaj numer VIN pojazdu, ktory chcesz usunac.");
                        carVin = scanner.nextInt();
                        currentUser.removeVehicle(carVin);
                    }else if(option == 3){
                        currentUser.showAvailableVehicles();
                    }else if(option == 4){
                        currentUser.showUserStatistics();
                    }else if(option == 5){
                        do{
                            System.out.println("Podaj kategorie prawa jazdy potrzebna do jezdzenia pojazdem, ktory chcesz wypozyczyc.");
                            category = scanner.next();
                        }while(!(category.equalsIgnoreCase("B") || category.equalsIgnoreCase("A") || category.equalsIgnoreCase("AM") || category.equalsIgnoreCase("A1") || category.equalsIgnoreCase("A2")));

                        System.out.println("Podaj cene pojazdu, ktorej nie chcialbys przekraczac.");
                        int priceLimit = scanner.nextInt();

                        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedBrand = scanner.next();

                        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedModel = scanner.next();
                        currentUser.rentVehicle(priceLimit, prefferedBrand, prefferedModel, category);
                    }else if(option == 6){
                        System.out.println("Podaj numer VIN pojazdu, ktory chcesz zwrocic. Numer VIN mozesz znalezc na dokumencie potwierdzajacym wypozyczenie pojazdu.");
                        carVin = scanner.nextInt();
                        currentUser.returnVehicle(carVin);
                    }else if(option == 7){
                        isLoggedIn = false;
                        System.out.println("Wylogowano");
                    }else{
                        System.out.println("Dziekujemy za skorzystanie z naszych uslug.");
                        break;
                    }
                }else {
                    System.out.println("""
                            Jesli chcesz wypozyczyc pojazd wybierz 1.
                            Jesli chcesz zwrocic pojazd wybierz 2.
                            Jesli chcesz wyswietlic statystyki swojego konta wybierz 3.
                            Jesli chcesz sie wylogowac wybierz inna liczbe.""");
                    option = scanner.nextInt();
                    if (option == 1) {
                        do {
                            System.out.println("Podaj kategorie prawa jazdy potrzebna do jezdzenia pojazdem, ktory chcesz wypozyczyc.");
                            category = scanner.next();
                        } while (!(category.equalsIgnoreCase("B") || category.equalsIgnoreCase("A") || category.equalsIgnoreCase("AM") || category.equalsIgnoreCase("A1") || category.equalsIgnoreCase("A2")));

                        System.out.println("Podaj cene pojazdu, ktorej nie chcialbys przekraczac.");
                        int priceLimit = scanner.nextInt();

                        System.out.println("Podaj marke pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedBrand = scanner.next();

                        System.out.println("Podaj model pojazdu jaki chcialbys wypozyczyc.");
                        String prefferedModel = scanner.next();
                        currentUser.rentVehicle(priceLimit, prefferedBrand, prefferedModel, category);
                    } else if (option == 2) {
                        System.out.println("Podaj numer VIN pojazdu, ktory chcesz zwrocic. Numer VIN mozesz znalezc na dokumencie potwierdzajacym wypozyczenie pojazdu.");
                        carVin = scanner.nextInt();
                        currentUser.returnVehicle(carVin);
                    }else if(option == 3){
                        currentUser.showUserStatistics();
                    }else{
                        isLoggedIn = false;
                        System.out.println("Wylogowano");
                    }
                }
            }
        }
    }
}