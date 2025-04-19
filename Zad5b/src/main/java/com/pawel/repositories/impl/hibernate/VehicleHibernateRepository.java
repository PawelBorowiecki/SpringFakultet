package com.pawel.repositories.impl.hibernate;

import com.pawel.models.Vehicle;
import com.pawel.repositories.VehicleRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class VehicleHibernateRepository implements VehicleRepository {
    private Session session;

    public void setSession(Session session){
        this.session = session;
    }

    @Override
    public List<Vehicle> findAll() {
        return session.createQuery("FROM Vehicle", Vehicle.class).list();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return Optional.ofNullable(session.get(Vehicle.class, id));
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return session.merge(vehicle);
    }

    @Override
    public void deleteById(String id) {
        Vehicle vehicle = session.get(Vehicle.class, id);
        if(vehicle != null){
            session.remove(vehicle);
        }
    }

    @Override
    public Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice) {
        Query<Vehicle> query = session.createQuery("""
            FROM Vehicle v
            WHERE v.brand = :brand
            AND v.model = :model
            AND v.price <= :price
            """, Vehicle.class);
        query.setParameter("brand", prefferedBrand);
        query.setParameter("model", prefferedModel);
        query.setParameter("price", prefferedPrice);

        return query.uniqueResultOptional();
    }
}
