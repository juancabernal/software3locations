package com.co.eatupapi.messaging.inventory.product;

import com.co.eatupapi.dto.inventory.product.ProductRequestDTO;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductEventPublisherBroker implements ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ProductQueueConfig productQueueConfig;

    public ProductEventPublisherBroker(RabbitTemplate rabbitTemplate,
                                       ProductQueueConfig productQueueConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.productQueueConfig = productQueueConfig;
    }

    @Override
    public void publishCreateRequested(ProductRequestDTO request) {

        String idMessage = UUID.randomUUID().toString();

        rabbitTemplate.convertAndSend(
                productQueueConfig.getProductExchangeName(),
                productQueueConfig.getCreateRoutingKey(),
                request,
                message -> {
                    message.getMessageProperties()
                            .setContentType(MessageProperties.CONTENT_TYPE_JSON);

                    message.getMessageProperties()
                            .setHeader("messageId", idMessage);
                    message.getMessageProperties()
                            .setHeader("eventType", "PRODUCT_CREATE");

                    return message;
                }
        );
    }
}
