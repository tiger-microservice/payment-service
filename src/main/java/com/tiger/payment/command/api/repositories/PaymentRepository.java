package com.tiger.payment.command.api.repositories;

import com.tiger.payment.command.api.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
