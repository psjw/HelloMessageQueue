package com.psjw.hellomessagequeue.step3;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;


    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "", message); // Fanout에서 routing key는 무시됨
        System.out.println("[#] Published notification : " + message);
    }
}
