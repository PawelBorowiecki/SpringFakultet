package com.pawel.repositories.impl.json;

import com.pawel.repositories.VehicleRepository;
import com.google.gson.reflect.TypeToken;
import com.pawel.models.Vehicle;
import com.pawel.db.JsonFileStorage;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VehicleJsonRepository implements VehicleRepository {
    private final JsonFileStorage<Vehicle> storage = new JsonFileStorage<>("vehicles.json", new TypeToken<List<Vehicle>>(){}.getType());
    private final List<Vehicle> vehicles;

    public VehicleJsonRepository(){
        this.vehicles = new ArrayList<>(storage.load());
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if(vehicle.getId() == null || vehicle.getId().isBlank()){
            vehicle.setId(UUID.randomUUID().toString() + LocalTime.now().toString());
        }else{
            deleteById(vehicle.getId());
        }
        vehicles.add(vehicle);
        storage.save(vehicles);
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        vehicles.removeIf(v -> id.equals(v.getId()));
        storage.save(vehicles);
    }
    @Override
    public Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice){
        return vehicles.stream().filter(v -> v.getBrand().equals(prefferedBrand) && v.getModel().equals(prefferedModel) && v.getPrice() <= prefferedPrice).findFirst();
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        storage.save(vehicles);
    }
}
