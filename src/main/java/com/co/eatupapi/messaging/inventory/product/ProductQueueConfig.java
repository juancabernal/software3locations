package com.co.eatupapi.messaging.inventory.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductQueueConfig {

    // =========================
    // EXCHANGE
    // =========================
    @Value("${rabbitmq.exchange.product}")
    private String productExchangeName;

    // =========================
    // ROUTING KEYS
    // =========================
    @Value("${rabbitmq.routing-key.product.create}")
    private String createRoutingKey;

    @Value("${rabbitmq.routing-key.product.update}")
    private String updateRoutingKey;

    @Value("${rabbitmq.routing-key.product.stock}")
    private String stockRoutingKey;

    @Value("${rabbitmq.routing-key.product.delete}")
    private String deleteRoutingKey;

    public String getProductExchangeName() {
        return productExchangeName;
    }

    public String getCreateRoutingKey() {
        return createRoutingKey;
    }

    public String getUpdateRoutingKey() {
        return updateRoutingKey;
    }

    public String getStockRoutingKey() {
        return stockRoutingKey;
    }

    public String getDeleteRoutingKey() {
        return deleteRoutingKey;
    }
}