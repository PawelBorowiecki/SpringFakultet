package com.pawel;

public abstract class Vehicle {
    protected final String brand, model;
    protected final int year, vin, price;
    protected boolean rented;
    public Vehicle(String brand, String model, int year, int vin, int price, boolean rented) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.price = price;
        this.rented = rented;
    }

    protected abstract String toCsv();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", vin=" + vin +
                ", price=" + price +
                ", rented=" + rented +
                '}';
    }
}
