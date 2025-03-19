package com.pawel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository implements IVehicleRepository{
    private List<Vehicle> vehicles = new ArrayList<>();
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
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && (parameters[6].equals("category='B'}") || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("A") || category.equals("a")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && (parameters[6].equals("category='A'}") || parameters[6].equals("category='A1'}") || parameters[6].equals("category='A2'}") || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("AM") || category.equals("am") || category.equals("Am") || category.equals("aM")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && parameters[6].equals("category='AM'}")){
                    v.rented = true;
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }else if(category.equals("A1") || category.equals("a1")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && (parameters[6].equals("category='A1'}")  || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
                    save();
                    return "Wypozyczono pojazd o podanych parametrach: \n" + v.toString();
                }
            }
        }
        else if(category.equals("A2") || category.equals("a2")){
            for(Vehicle v : this.vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && (parameters[6].equals("category='A2'}")|| parameters[6].equals("category='A1'}") || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
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
            if(vin == v.vin && v.rented){
                v.rented = false;
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
        return this.vehicles.stream().filter(v -> v.vin == carId).toList().getFirst();
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
                    Car car = new Car(v.brand, v.model, v.year, v.vin, v.price, v.rented, "B");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", car.brand, car.model, car.vin, car.year, car.price, car.rented, car.category);
                }else if(params[6].equals("category='A'}")){
                    Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                }else if(params[6].equals("category='AM'}")){
                    Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "AM");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                }else if(params[6].equals("category='A1'}")){
                    Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A1");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                }else{
                    Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A2");
                    line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
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
    public void removeVehicle(int vin) {
        this.vehicles.removeIf(v -> v.vin == vin);
        save();
    }
}
