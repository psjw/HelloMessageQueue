package com.psjw.hellomessagequeue.step3;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class StompController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public StompController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send")
    public void send(NotificationMessage notificationMessage) {
        // 수신된 메시지를 브로드캐스팅
        String message = notificationMessage.getMessage();

        System.out.println("[#] message = " + message);
        //클라이언트에 메세지 브로드캐스트
        simpMessagingTemplate.convertAndSend(NotificationSubscriber.CLIENT_URL, message);
    }
}
