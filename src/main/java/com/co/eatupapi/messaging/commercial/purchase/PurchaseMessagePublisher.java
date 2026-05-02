package com.co.eatupapi.messaging.commercial.purchase;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.commercial}")
    private String exchange;

    @Value("${rabbitmq.routing-key.purchase}")
    private String routingKey;

    public PurchaseMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPurchaseReceived(PurchaseMessage event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}