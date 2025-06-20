package org.pawel.zad9.repository;

import org.pawel.zad9.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByStripeSessionId(String stripeSessionId);
}
