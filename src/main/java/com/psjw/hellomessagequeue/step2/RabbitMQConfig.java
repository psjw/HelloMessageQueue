package com.psjw.hellomessagequeue.step2;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {
    // 큐 네임을 설정한다.
    public static final String QUEUE_NAME = "WorkQueue";

    @Bean
    public Queue queue() {
        //durable : 휘발성이냐 아니냐 volatile(휘발성 : false)  or persistent(영속성 : true) 라고 함
        //서버가 shutdown 됬을때 데이터 보관하는 옵션
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        //ConnectionFactory가 RabbitMQ의 연결을 관리하는 객체
        return new RabbitTemplate(connectionFactory);
    }


    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(WorkQueueConsumer workQueueTask) {
        return new MessageListenerAdapter(workQueueTask, "workQueueTask");
    }
}
