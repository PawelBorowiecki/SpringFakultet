package com.pawel.userutils;

import com.pawel.repository.UserRepository;
import com.pawel.repository.VehicleRepository;
import com.pawel.vehicles.Vehicle;

import java.util.List;

public class User {
    private final String login, password;
    private final Role role;
    private int carId = -1;
    private static final UserRepository userRepository = new UserRepository();
    private static final VehicleRepository vehicleRepository = new VehicleRepository();

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
        if(vin == User.userRepository.getUser(this.login).getCarId()){
            String returnStatus = User.vehicleRepository.returnVehicle(vin);
            this.carId = -1;
            User.userRepository.changeCarStatus(this);
            System.out.println(returnStatus);
        }else{
            System.out.println("Nie masz wypozyczonego pojazdu o podanym numerze VIN.");
        }
    }

    public void addVehicle(Vehicle vehicle){
        if(this.role.equals(Role.ADMIN)){
            if(!User.vehicleRepository.containsVehicle(vehicle.getVin())){
                User.vehicleRepository.addVehicle(vehicle);
            }else{
                System.out.println("Pojazd o podanym numerze VIN juz istnieje. Sprobuj ponownie dodac pojazd podajac wlasciwe dane.");
            }
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
        if(this.role.equals(Role.ADMIN)){
            List<User> users = User.userRepository.getUsers();
            for(User u : users){
                System.out.println("Login: " + u.login + "\nPassword: " + u.password + "\nRole: " + u.role + "\nRented vehicle: ");
                if(u.carId >= 0){
                    System.out.println(User.vehicleRepository.getVehicle(u.carId).toString());
                }
            }
        }else{
            User u = userRepository.getUser(this.login);
            System.out.println("Login: " + u.login + "\nPassword: " + u.password + "\nRole: " + u.role + "\nRented vehicle: ");
            if(u.carId >= 0){
                System.out.println(User.vehicleRepository.getVehicle(u.carId).toString());
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
