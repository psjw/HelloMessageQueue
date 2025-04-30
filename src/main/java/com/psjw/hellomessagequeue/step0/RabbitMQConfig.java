package com.psjw.hellomessagequeue.step0;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 큐 네임을 설정한다.
    public static final String QUEUE_NAME = "helloqueue";

    @Bean
    public Queue queue() {
        //durable : 휘발성이냐 아니냐 volatile(휘발성 : false)  or persistent(영속성 : true) 라고 함
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //ConnectionFactory가 RabbitMQ의 연결을 관리하는 객체
        return new RabbitTemplate(connectionFactory);
    }
}
