package com.pawel.vehicles;

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

    public abstract String toCsv();

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public int getVin() {
        return vin;
    }

    public int getPrice() {
        return price;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }
}
