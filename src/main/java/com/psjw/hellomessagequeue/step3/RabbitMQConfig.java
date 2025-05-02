package com.psjw.hellomessagequeue.step3;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {
    protected static final String QUEUE_NAME = "notificationQueue";
    protected static final String EXCHANGE_NAME = "notificationExchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, false); //메시지는 volatile로 설정
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        //메시지를 수신하면 연결된 모든 큐로 브로드캐스트
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindNotification(Queue notificationQueue, FanoutExchange fanoutExchange) {
        //BindingBuilder.bind().to()를 통해 큐와 익스체인지를 연결
        return BindingBuilder.bind(notificationQueue).to(fanoutExchange);
    }

}
