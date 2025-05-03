package com.psjw.hellomessagequeue.step7;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;


/**
 * RabbitMQ 리스너 설정을 커스터마이징 한다.
 */
@EnableRabbit
//@Configuration
public class RabbitMQManualConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //수정 모드 설정으로 들어가야한다.
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}
