package com.pawel;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserController userController = new UserController();
        int option;
        String category, params;

        System.out.println("Witaj w wypozyczalni pojazdow!");
        while(true){
            System.out.println("""
                    Jesli chcesz wypozyczyc pojazd wybierz 1.
                    Jesli chcesz zwrocic pojazd wybierz 2.
                    Jesli chcesz zobaczyc flote pojazdow jakimi dysponujemy wybierz 3.
                    Jesli chcesz wyjsc z wypozyczalni wybierz inna liczbe.""");

            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();
            if(option == 1){
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
                params = userController.rentVehicle(priceLimit, prefferedBrand, prefferedModel, category);
                if(!params.isEmpty()){
                    System.out.println("Wypozyczono pojazd o podanych parametrach:\n" + params);
                }else{
                    System.out.println("Nie udalo sie znalezc pojazdu spelniajacego Twoje wymagania.");
                }
            }else if(option == 2){
                System.out.println("Podaj numer VIN pojazdu, ktory chcesz zwrocic. Numer VIN mozesz znalezc na dokumencie potwierdzajacym wypozyczenie pojazdu.");
                int vin = scanner.nextInt();
                params = userController.returnVehicle(vin);
                if(!params.isEmpty()){
                    System.out.println("Zwrocono pojazd o podanych parametrach:\n" + params);
                }else{
                    System.out.println("Nie udalo sie zwrocic pojazdu o numerze VIN: " + vin + ". Sprobuj ponownie przeprowadzic operacje zwrotu.");
                }
            }else if(option == 3){
                System.out.println("Nasza flota to:");
                List<Vehicle> fleet = userController.getVehicles();
                for(Vehicle v : fleet){
                    System.out.println(v.toString());
                }
            }else{
                System.out.println("Dziekujemy za skorzystanie z naszych uslug. Do zobaczenia.");
                break;
            }
        }
    }
}