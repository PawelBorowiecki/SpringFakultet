package com.pawel.vehicles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Car extends Vehicle{
    public Car(String brand, String model, int year, int vin, int price, boolean rented, String category) {
        super(brand, model, year, vin, price, rented, category);
    }

    @Override
    public String toCsv() {
        File file = new File("vehicles.csv");
        String line;
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
            if(!file.exists()){
                file.createNewFile();
            }
            line = String.format("%s;%s;%d;%d;%d;%b;%s\n", this.brand, this.model, this.vin, this.year, this.price, this.rented, this.category);
            bufferedWriter.append(line);
        } catch (IOException e) {
            throw new RuntimeException("Motorcycle toCsv error.");
        }
        return line;
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
                ", category='" + category + '\'' +
                '}';
    }
}
