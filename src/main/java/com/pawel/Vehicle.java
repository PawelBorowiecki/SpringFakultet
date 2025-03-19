package com.pawel;

public abstract class Vehicle {
    protected String brand, model, category;
    protected int year, vin, price;
    protected boolean rented;
    public Vehicle(String brand, String model, int year, int vin, int price, boolean rented, String category) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.price = price;
        this.rented = rented;
        this.category = category;
    }

    protected abstract String toCsv();
}
