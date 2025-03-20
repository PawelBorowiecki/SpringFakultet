package com.pawel;

import java.util.List;

public class User {
    private String login, password;
    private Role role;
    private int carId = -1;
    private static UserRepository userRepository = new UserRepository();
    private static VehicleRepository vehicleRepository = new VehicleRepository();

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, Role role, int carId) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.carId = carId;
    }

    public boolean signIn(){
        if(Authentication.isLoginDataCorrect(this.login, this.password, this.role)){
            System.out.println("Zalogowano.");
            return true;
        }
        System.out.println("Nie udalo sie zalogowac. Sprobuj ponownie.");
        return false;
    }

    public void register(){
        User.userRepository.addUser(this);
    }

    public void rentVehicle(int priceLimit, String prefferedBrand, String prefferedModel, String category){
        String rentStatus = User.vehicleRepository.rentVehicle(priceLimit, prefferedBrand, prefferedModel, category);
        if(!rentStatus.equals("Nie udalo sie znalezc pojazdu spelniajacego Twoje wymagania.")){
            String[] params = rentStatus.split(" ");
            this.carId = Integer.parseInt(params[8].substring(4, 9));
            User.userRepository.changeCarStatus(this);
        }
        System.out.println(rentStatus);
    }

    public void returnVehicle(int vin){
        String returnStatus = User.vehicleRepository.returnVehicle(vin);
        this.carId = -1;
        User.userRepository.changeCarStatus(this);
        System.out.println(returnStatus);
    }

    public void addVehicle(Vehicle vehicle){
        if(this.role.equals(Role.ADMIN)){
            User.vehicleRepository.addVehicle(vehicle);
        }else{
            System.out.println("Nie masz uprawnien do wykonania tej operacji.");
        }
    }

    public void removeVehicle(int vin){
        if(this.role.equals(Role.ADMIN)){
            boolean removeStatus = User.vehicleRepository.removeVehicle(vin);
            if(removeStatus){
                System.out.println("Usunieto pojazd o numerze VIN: " + vin);
            }else{
                System.out.println("Nie udalo sie usunac pojazdu.");
            }
        }else{
            System.out.println("Nie masz uprawnien do wykonania tej operacji.");
        }
    }

    public void showAvailableVehicles(){
        if(this.role.equals(Role.ADMIN)){
            List<Vehicle> fleet = vehicleRepository.getVehicles();
            for(Vehicle v : fleet){
                System.out.println(v.toString());
            }
        }else{
            System.out.println("Nie masz uprawnien do wykonania tej operacji.");
        }
    }

    public void showUserStatistics(){
        List<User> users = User.userRepository.getUsers();

        for(User u : users) {
            if(u.login.equals(this.login) && this.role.equals(Role.NORMAL)) {
                System.out.println("Login: " + u.login + "\nPassword: " + u.password + "\nRole: " + u.role + "\nRented vehicle: ");
                if(u.carId >= 0){
                    System.out.println(User.vehicleRepository.getVehicle(u.carId).toString());
                }
            }else if(this.role.equals(Role.ADMIN)){
                System.out.println("Login: " + u.login + "\nPassword: " + u.password + "\nRole: " + u.role + "\nRented vehicle: ");
                if(u.carId >= 0){
                    System.out.println(User.vehicleRepository.getVehicle(u.carId).toString());
                }
            }
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
