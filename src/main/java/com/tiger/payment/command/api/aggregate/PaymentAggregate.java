package com.tiger.payment.command.api.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import vn.tiger.sagacommon.commands.CancelPaymentCommand;
import vn.tiger.sagacommon.commands.ValidatePaymentCommand;
import vn.tiger.sagacommon.events.PaymentCancelledEvent;
import vn.tiger.sagacommon.events.PaymentProcessedEvent;

@Slf4j
@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    private String paymentStatus;

    public PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
        // Validate the payment details
        // publish the payment processed event
        log.info("Executing ValidatePaymentCommand for OrderId {} and PaymentId {}",
                validatePaymentCommand.getOrderId(),
                validatePaymentCommand.getPaymentId());

        PaymentProcessedEvent paymentProcessedEvent
                = new PaymentProcessedEvent(validatePaymentCommand.getPaymentId(),validatePaymentCommand.getOrderId());

        AggregateLifecycle.apply(paymentProcessedEvent);
        log.info("PaymentProcessedEvent applied");
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.getPaymentId();
        this.orderId = event.getOrderId();
    }

    @CommandHandler
    public void handle(CancelPaymentCommand cancelPaymentCommand) {
        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent();
        BeanUtils.copyProperties(cancelPaymentCommand, paymentCancelledEvent);
        AggregateLifecycle.apply(paymentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(PaymentCancelledEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
