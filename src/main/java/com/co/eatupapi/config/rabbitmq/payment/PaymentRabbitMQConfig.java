package com.co.eatupapi.config.rabbitmq.payment;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentRabbitMQConfig {

    @Value("${rabbitmq.exchange.payment}")
    private String exchangeName;

    @Value("${rabbitmq.queue.payment.cashreceipt.create}")
    private String createQueueName;

    @Value("${rabbitmq.queue.payment.cashreceipt.cancel}")
    private String cancelQueueName;

    @Value("${rabbitmq.routing-key.payment.cashreceipt.create}")
    private String createRoutingKey;

    @Value("${rabbitmq.routing-key.payment.cashreceipt.cancel}")
    private String cancelRoutingKey;

    @Bean
    public RabbitAdmin rabbitAdminPayment(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.initialize();
        return admin;
    }

    @Bean
    public MessageConverter paymentJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate paymentRabbitTemplate(ConnectionFactory connectionFactory,
                                                MessageConverter paymentJsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(paymentJsonMessageConverter);
        return template;
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue paymentCashReceiptCreateQueue() {
        return QueueBuilder.durable(createQueueName).build();
    }

    @Bean
    public Queue paymentCashReceiptCancelQueue() {
        return QueueBuilder.durable(cancelQueueName).build();
    }

    @Bean
    public Binding paymentCashReceiptCreateBinding(Queue paymentCashReceiptCreateQueue, DirectExchange paymentExchange) {
        return BindingBuilder
                .bind(paymentCashReceiptCreateQueue)
                .to(paymentExchange)
                .with(createRoutingKey);
    }

    @Bean
    public Binding paymentCashReceiptCancelBinding(Queue paymentCashReceiptCancelQueue, DirectExchange paymentExchange) {
        return BindingBuilder
                .bind(paymentCashReceiptCancelQueue)
                .to(paymentExchange)
                .with(cancelRoutingKey);
    }
}
