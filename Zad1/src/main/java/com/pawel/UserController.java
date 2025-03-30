package com.pawel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserController implements IVehicleRepository{
    public UserController() {
        for(Vehicle v : vehicles){
            v.toCsv();
        }
    }

    @Override
    public String rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category) {
        List<Vehicle> vehicles = getVehicles();
        String[] parameters;

        if(category.equals("B") || category.equals("b")){
            for(Vehicle v : vehicles){
                parameters = v.toString().split(" ");
                if(((v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && parameters.length == 6) || (v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && parameters.length == 7 && parameters[6].equals("category='AM'}"))) && v.price <= priceLimit && !v.rented){
                    v.rented = true;
                    save(vehicles);
                    return v.toString();
                }
            }
        }else if(category.equals("A") || category.equals("a")){
            for(Vehicle v : vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && parameters.length == 7){
                    v.rented = true;
                    save(vehicles);
                    return v.toString();
                }
            }
        }else if(category.equals("AM") || category.equals("am") || category.equals("Am") || category.equals("aM")){
            for(Vehicle v : vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && parameters.length == 7 && parameters[6].equals("category='AM'}")){
                    v.rented = true;
                    save(vehicles);
                    return v.toString();
                }
            }
        }else if(category.equals("A1") || category.equals("a1")){
            for(Vehicle v : vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && parameters.length == 7 && (parameters[6].equals("category='A1'}")  || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
                    save(vehicles);
                    return v.toString();
                }
            }
        }
        else if(category.equals("A2") || category.equals("a2")){
            for(Vehicle v : vehicles){
                parameters = v.toString().split(" ");
                if(v.brand.equals(prefferedBrand) && v.model.equals(prefferedModel) && v.price <= priceLimit && !v.rented && parameters.length == 7 && (parameters[6].equals("category='A2'}")|| parameters[6].equals("category='A1'}") || parameters[6].equals("category='AM'}"))){
                    v.rented = true;
                    save(vehicles);
                    return v.toString();
                }
            }
        }
        return "";
    }

    @Override
    public String returnVehicle(int vin) {
        List<Vehicle> vehicles = getVehicles();
        for(Vehicle v : vehicles){
            if(vin == v.vin && v.rented){
                v.rented = false;
                save(vehicles);
                return v.toString();
            }
        }
        return "";
    }

    @Override
    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("vehicles.csv"))) {
            String line, brand, model;
            int year, vin, price;
            boolean isRented;
            while((line = bufferedReader.readLine()) != null){
                String[] parameters = line.split(";");
                brand = parameters[0];
                model = parameters[1];
                vin = Integer.parseInt(parameters[2]);
                year = Integer.parseInt(parameters[3]);
                price = Integer.parseInt(parameters[4]);
                isRented = Boolean.parseBoolean(parameters[5]);
                if(parameters.length == 7){
                    String category = parameters[6];
                    Motorcycle motorcycle = new Motorcycle(brand, model, year, vin, price, isRented, category);
                    vehicles.add(motorcycle);
                }else{
                    Car car = new Car(brand, model, year, vin, price, isRented);
                    vehicles.add(car);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("UC getVehicles error.");
        }

        return vehicles;
    }

    @Override
    public void save(List<Vehicle> vehicles) {
        File file = new File("vehicles.csv");
        String line;
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(file))) {
            if(!file.exists()){
                file.createNewFile();
            }
            for(Vehicle v : vehicles){
                String[] params = v.toString().split(" ");
                if(params.length == 6){
                    line = String.format("%s;%s;%d;%d;%d;%b\n", v.brand, v.model, v.vin, v.year, v.price, v.rented);
                }else{
                    if(params[6].length() == 14){
                        if(params[6].charAt(11) == 'M'){
                            Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "AM");
                            line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                        }else if(params[6].charAt(11) == '1'){
                            Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A1");
                            line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                        }else{
                            Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A2");
                            line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                        }
                    }else {
                        Motorcycle moto = new Motorcycle(v.brand, v.model, v.year, v.vin, v.price, v.rented, "A");
                        line = String.format("%s;%s;%d;%d;%d;%b;%s\n", moto.brand, moto.model, moto.vin, moto.year, moto.price, moto.rented, moto.category);
                    }
                }
                printWriter.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("UC save error.");
        }
    }
}