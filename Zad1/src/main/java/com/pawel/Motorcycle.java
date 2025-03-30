package com.pawel;

import java.io.*;

public class Motorcycle extends Vehicle{
    protected final String category;

    public Motorcycle(String brand, String model, int year, int vin, int price, boolean rented, String category) {
        super(brand, model, year, vin, price, rented);
        this.category = category;
    }

    @Override
    protected String toCsv() {
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
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object vehicle) {
        if(vehicle == null){
            return false;
        }
        String[] params = vehicle.toString().split(" ");
        if(vehicle.getClass() == Motorcycle.class){
            return (params[0].equals("Motorcycle{brand='" + this.brand + "',") && params[1].equals("model='" + this.model + "',")
                    && params[2].equals("year='" + this.year + ",") && params[3].equals("vin='" + this.vin + ",")
                    && params[4].equals("price=" + this.price + ",") && params[5].equals("rented='" + this.rented + ",")
                    && params[6].equals("category='" + this.category + "'}"));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
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
