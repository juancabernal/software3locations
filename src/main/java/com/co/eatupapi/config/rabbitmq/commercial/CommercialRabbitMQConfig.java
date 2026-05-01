package com.co.eatupapi.config.rabbitmq.commercial;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommercialRabbitMQConfig {

    @Value("${rabbitmq.exchange.commercial}")
    private String exchangeName;

    @Value("${rabbitmq.queue.purchase}")
    private String queueName;

    @Value("${rabbitmq.routing-key.purchase}")
    private String routingKey;


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.initialize();
        return admin;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public DirectExchange commercialExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue purchaseQueue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding purchaseBinding(Queue purchaseQueue, DirectExchange commercialExchange) {
        return BindingBuilder
                .bind(purchaseQueue)
                .to(commercialExchange)
                .with(routingKey);
    }
}