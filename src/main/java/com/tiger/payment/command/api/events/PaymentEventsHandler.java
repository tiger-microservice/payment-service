package com.tiger.payment.command.api.events;

import com.tiger.payment.command.api.entities.Payment;
import com.tiger.payment.command.api.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import vn.tiger.sagacommon.events.PaymentCancelledEvent;
import vn.tiger.sagacommon.events.PaymentProcessedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder().build();

        BeanUtils.copyProperties(event, payment);
        payment.setPaymentId(UUID.fromString(event.getPaymentId()));
        payment.setPaymentStatus("COMPLETED");
        payment.setExecuteTime(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(UUID.fromString(event.getPaymentId())).get();
        payment.setPaymentStatus(event.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
