package com.co.eatupapi.messaging.payment.cashreceipt;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CashReceiptMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.payment}")
    private String exchange;

    @Value("${rabbitmq.routing-key.payment.cashreceipt.create}")
    private String createRoutingKey;

    @Value("${rabbitmq.routing-key.payment.cashreceipt.cancel}")
    private String cancelRoutingKey;

    public CashReceiptMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreate(CashReceiptCreateMessage event) {
        rabbitTemplate.convertAndSend(exchange, createRoutingKey, event);
    }

    public void publishCancel(CashReceiptCancelMessage event) {
        rabbitTemplate.convertAndSend(exchange, cancelRoutingKey, event);
    }
}
