package com.pawel.repository;

import com.pawel.vehicles.Car;
import com.pawel.vehicles.Motorcycle;
import com.pawel.vehicles.Vehicle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository{
    private final List<Vehicle> vehicles = new ArrayList<>();
    public VehicleRepository() {
        for(Vehicle v : this.vehicles){
            v.toCsv();
        }
    }

    @Override
    public String rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category) {
        String[] parameters;
        if(category.equals("B") || category.equals("b")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= priceLimit && !v.isRented() && (parameters[6].equals("category='B'}") || parameters[6].equals("category='AM'}"))){
                    v.setRented(true);
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("A") || category.equals("a")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= priceLimit && !v.isRented() && (parameters[6].equals("category='A'}") || parameters[6].equals("category='A1'}") || parameters[6].equals("category='A2'}") || parameters[6].equals("category='AM'}"))){
                    v.setRented(true);
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("AM") || category.equals("am") || category.equals("Am") || category.equals("aM")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= priceLimit && !v.isRented() && parameters[6].equals("category='AM'}")){
                    v.setRented(true);
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("A1") || category.equals("a1")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= priceLimit && !v.isRented() && (parameters[6].equals("category='A1'}")  || parameters[6].equals("category='AM'}"))){
                    v.setRented(true);
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }
        else if(category.equals("A2") || category.equals("a2")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= priceLimit && !v.isRented() && (parameters[6].equals("category='A2'}")|| parameters[6].equals("category='A1'}") || parameters[6].equals("category='AM'}"))){
                    v.setRented(true);
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }
        return "Nie udalo sie znalezc pojazdu spelniajacego Twoje wymagania.";
    }

    @Override
    public String returnVehicle(int vin) {
        for(Vehicle v : this.vehicles){
            if(vin == v.getVin() && v.isRented()){
                v.setRented(false);
                save();
                return "Zwrocono pojazd o podanych parametrach:\n" + v.toString();
            }
        }
        return "Nie udalo sie zwrocic pojazdu o numerze VIN: " + vin + ". Sprobuj ponownie przeprowadzic operacje zwrotu.";
    }

    @Override
    public List<Vehicle> getVehicles() {
        return this.vehicles;
    }

    @Override
    public Vehicle getVehicle(int carId) {
        return this.vehicles.stream().filter(v -> v.getVin() == carId).toList().getFirst();
    }

    @Override
    public void save() {
        File file = new File("vehicles.csv");
        String line;
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file))) {
            if(!file.exists()){
                file.createNewFile();
            }
            for(Vehicle v : this.vehicles){
                String[] params = v.toString().split(" ");
                if(params[6].equals("category='B'}")){
                    Car car = new Car(v.getBrand(), v.getModel(), v.getYear(), v.getVin(), v.getPrice(), v.isRented(), "B");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", car.getBrand(), car.getModel(), car.getVin(), car.getYear(), car.getPrice(), car.isRented(), car.getCategory());
                }else if(params[6].equals("category='A'}")){
                    Motorcycle moto = new Motorcycle(v.getBrand(), v.getModel(), v.getYear(), v.getVin(), v.getPrice(), v.isRented(), "A");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.getBrand(), moto.getModel(), moto.getVin(), moto.getYear(), moto.getPrice(), moto.isRented(), moto.getCategory());
                }else if(params[6].equals("category='AM'}")){
                    Motorcycle moto = new Motorcycle(v.getBrand(), v.getModel(), v.getYear(), v.getVin(), v.getPrice(), v.isRented(), "AM");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.getBrand(), moto.getModel(), moto.getVin(), moto.getYear(), moto.getPrice(), moto.isRented(), moto.getCategory());
                }else if(params[6].equals("category='A1'}")){
                    Motorcycle moto = new Motorcycle(v.getBrand(), v.getModel(), v.getYear(), v.getVin(), v.getPrice(), v.isRented(), "A1");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.getBrand(), moto.getModel(), moto.getVin(), moto.getYear(), moto.getPrice(), moto.isRented(), moto.getCategory());
                }else{
                    Motorcycle moto = new Motorcycle(v.getBrand(), v.getModel(), v.getYear(), v.getVin(), v.getPrice(), v.isRented(), "A2");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.getBrand(), moto.getModel(), moto.getVin(), moto.getYear(), moto.getPrice(), moto.isRented(), moto.getCategory());
                }
                printWriter.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("UC save error.");
        }
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        save();
    }

    @Override
    public boolean removeVehicle(int vin) {
        boolean removeStatus = this.vehicles.removeIf(v -> v.getVin() == vin && !v.isRented());
        save();
        return removeStatus;
    }

    public boolean containsVehicle(int vin){
        for(Vehicle v : this.vehicles){
            if(v.getVin() == vin){
                return true;
            }
        }
        return false;
    }
}
