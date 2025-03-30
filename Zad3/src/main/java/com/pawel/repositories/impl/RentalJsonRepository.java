package com.pawel.repositories.impl;

import com.google.gson.reflect.TypeToken;
import com.pawel.models.Rental;
import com.pawel.repositories.RentalRepository;
import com.pawel.utils.JsonFileStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJsonRepository implements RentalRepository {
    private final JsonFileStorage<Rental> storage = new JsonFileStorage<>("rentals.json", new TypeToken<List<Rental>>(){}.getType());
    private final List<Rental> rentals;

    public RentalJsonRepository(){
        this.rentals = new ArrayList<>(storage.load());
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public Optional<Rental> findById(String id) {
        return rentals.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    @Override
    public Rental save(Rental rental) {
        if(rental.getId() == null || rental.getId().isBlank()){
            rental.setId(UUID.randomUUID().toString());
        }else{
            deleteById(rental.getId());
        }
        rentals.add(rental);
        storage.save(rentals);
        return rental;
    }

    @Override
    public void deleteById(String id) {
        rentals.removeIf(r -> r.getId().equals(id));
        storage.save(rentals);
    }
}
