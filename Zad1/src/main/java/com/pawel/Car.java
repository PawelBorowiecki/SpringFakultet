package com.pawel;

import java.io.*;

public class Car extends Vehicle{
    public Car(String brand, String model, int year, int vin, int price, boolean rented){
        super(brand, model, year, vin, price, rented);
    }

    @Override
    protected String toCsv() {
        File file = new File("vehicles.csv");
        String line;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            if(!file.exists()){
                file.createNewFile();
            }
            line = String.format("%s;%s;%d;%d;%d;%b\n", this.brand, this.model, this.vin, this.year, this.price, this.rented);
            bufferedWriter.append(line);
        } catch (IOException e) {
            throw new RuntimeException("Car toCsv error.");
        }
        return line;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object vehicle) {
        if(vehicle == null){
            return false;
        }
        String[] params = vehicle.toString().split(" ");
        if(vehicle.getClass() == Car.class){
            return (params[0].equals("Car{brand='" + this.brand + "',") && params[1].equals("model='" + this.model + "',")
                    && params[2].equals("year=" + this.year + ",") && params[3].equals("vin=" + this.vin + ",")
                    && params[4].equals("price=" + this.price + ",") && params[5].equals("rented=" + this.rented + "}"));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", vin=" + vin +
                ", price=" + price +
                ", rented=" + rented +
                '}';
    }
}
