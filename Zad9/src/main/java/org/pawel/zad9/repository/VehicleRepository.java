package org.pawel.zad9.repository;


import org.pawel.zad9.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    @Query("SELECT v FROM Vehicle v WHERE v.brand = ?1 AND v.model = ?2 AND v.price <= ?3")
    Optional<Vehicle> findByPreferences(String prefferedBrand, String prefferedModel, double prefferedPrice);
    @Query("SELECT v FROM Vehicle v WHERE v.isActive = TRUE")
    List<Vehicle> findByIsActiveTrue();
    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1 AND v.isActive = TRUE")
    Optional<Vehicle> findByIdAndIsActiveTrue(String id);
    @Query("SELECT v FROM Vehicle v WHERE v.isActive = true AND v.id NOT IN(?1)")
    List<Vehicle> findByIsActiveTrueAndIdNotIn(Set<String> rentedVehicleIds);
}
