package com.pawel.services.impl.hibernate;

import com.pawel.db.HibernateConfig;
import com.pawel.models.Rental;
import com.pawel.models.Vehicle;
import com.pawel.repositories.impl.hibernate.RentalHibernateRepository;
import com.pawel.repositories.impl.hibernate.VehicleHibernateRepository;
import com.pawel.services.VehicleService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleHibernateService implements VehicleService {
    private final VehicleHibernateRepository vehicleRepo;
    private final RentalHibernateRepository rentalRepo;

    public VehicleHibernateService(VehicleHibernateRepository vehicleRepo, RentalHibernateRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            vehicleRepo.setSession(session);
            return vehicleRepo.findAll();
        }
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            vehicleRepo.setSession(session);
            return vehicleRepo.findById(id);
        }
    }

    @Override
    public Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            vehicleRepo.setSession(session);
            return vehicleRepo.findByPreferences(prefferedBrand, prefferedModel, prefferedPrice);
        }
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            vehicleRepo.save(vehicle);
            tx.commit();
            return vehicle;
        }
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        List<Vehicle> availableVehicles = new ArrayList<>();
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            vehicleRepo.setSession(session);
            rentalRepo.setSession(session);
            List<Vehicle> allVehicles = vehicleRepo.findAll();
            for(Vehicle v : allVehicles){
                Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(v.getId());
                if(rental.isEmpty()){
                    availableVehicles.add(v);
                }
            }
        }
        return availableVehicles;
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            rentalRepo.setSession(session);
            Optional<Rental> rental = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
            if(rental.isEmpty()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeVehicle(String id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            vehicleRepo.setSession(session);
            vehicleRepo.deleteById(id);
            tx.commit();
        }
    }
}
