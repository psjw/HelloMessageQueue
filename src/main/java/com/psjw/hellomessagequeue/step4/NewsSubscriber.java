package com.psjw.hellomessagequeue.step4;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewsSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    public NewsSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.JAVA_QUEUE)
    public void javaNes(String message){
        messagingTemplate.convertAndSend("/topic/java", message); // 뉴스 브로드캐스트
        System.out.println("[#] Received Java News: " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.SPRING_QUEUE)
    public void springNes(String message){
        messagingTemplate.convertAndSend("/topic/spring", message); // 기술 뉴스 브로드캐스트
        System.out.println("[#] Received Spring News: " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.VUE_QUEUE)
    public void vueNes(String message){
        messagingTemplate.convertAndSend("/topic/vue", message); // 일반 뉴스 브로드캐스트
        System.out.println("[#] Received Vue News: " + message);
    }
}
