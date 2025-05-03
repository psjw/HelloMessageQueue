package com.psjw.hellomessagequeue.step7;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import javax.imageio.IIOException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    private static final int MAX_RETRIES = 3; // 총 시도 제한수
    private int retryCount = 0; //재시도 횟수

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processOrder(String message, Channel channel, @Header("amqp_deliveryTag") long tag) {
        try{
            //실패 유발
            if("fail".equalsIgnoreCase(message)){
                if(retryCount < MAX_RETRIES){
                    System.out.println("### Fail & Retry : " + retryCount);
                    retryCount++;
                    throw new RuntimeException(message);
                }else{
                    System.err.println("### 최대 횟수 초과, DLQ 이동시킴.");
                    retryCount = 0;
                    channel.basicNack(tag, false, false); //requeue 현재 큐에 넣겠다.
                    return;
                }

            }
            //성공처리
            System.out.println("## 성공 : " + message);
            channel.basicAck(tag, false);
            retryCount = 0;
        } catch (Exception e) {
            System.err.println("# error 발생 : " + e.getMessage());
            try{
                //실패시 basicReject 재처리 전송
                channel.basicReject(tag, true);
            } catch (IOException ex) {
                System.err.println("# fail & reject message : " + ex.getMessage());
            }
        }
    }
}
