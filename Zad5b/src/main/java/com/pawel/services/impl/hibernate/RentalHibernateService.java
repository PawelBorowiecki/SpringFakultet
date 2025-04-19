package com.pawel.services.impl.hibernate;

import com.pawel.db.HibernateConfig;
import com.pawel.models.Rental;
import com.pawel.models.User;
import com.pawel.models.Vehicle;
import com.pawel.repositories.impl.hibernate.RentalHibernateRepository;
import com.pawel.repositories.impl.hibernate.UserHibernateRepository;
import com.pawel.repositories.impl.hibernate.VehicleHibernateRepository;
import com.pawel.services.RentalService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalHibernateService implements RentalService {
    private final RentalHibernateRepository rentalRepo;
    private final VehicleHibernateRepository vehicleRepo;
    private final UserHibernateRepository userRepo;

    public RentalHibernateService(RentalHibernateRepository rentalRepo, VehicleHibernateRepository vehicleRepo, UserHibernateRepository userRepo) {
        this.rentalRepo = rentalRepo;
        this.vehicleRepo = vehicleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            rentalRepo.setSession(session);
            return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent();
        }
    }

    @Override
    public Rental rent(String vehicleId, String userId) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);

            if(rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isPresent()){
                throw new IllegalStateException("Vehicle is rented");
            }

            Vehicle vehicle = vehicleRepo.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found."));
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));

            Rental rental = Rental.builder()
                    .id(UUID.randomUUID().toString())
                    .vehicle(vehicle)
                    .user(user)
                    .rentDate(LocalDateTime.now().toString())
                    .build();

            rentalRepo.save(rental);
            tx.commit();

            return rental;
        }catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
        }
        return null;
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            tx = session.beginTransaction();

            rentalRepo.setSession(session);
            vehicleRepo.setSession(session);
            userRepo.setSession(session);

            Optional<Rental> rentalFromDb = rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId);
            if(rentalFromDb.isEmpty()){
                throw new IllegalStateException("Vehicle is not rented");
            }

            rentalFromDb.get().setReturnDate(LocalDateTime.now().toString());
            rentalRepo.save(rentalFromDb.get());
            tx.commit();

            return true;
        }catch (Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
        }
        return false;
    }

    @Override
    public List<Rental> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            rentalRepo.setSession(session);
            return rentalRepo.findAll();
        }
    }
}
