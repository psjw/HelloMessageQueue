package com.psjw.hellomessagequeue.step11;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final StockRepository stockRepository;

    public MessageConsumer(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(StockEntity stock,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            Channel channel) {

        try {
            System.out.println("[Consumer]" + stock);
            //DB가 없다고 나오는 경우 : 같은 네트워크 구간이라 latency가 없어서 ->  sleep으로 처리
            Thread.sleep(200);
            Optional<StockEntity> optionalStock = stockRepository.findById(stock.getId());
            if (optionalStock.isPresent()) {
                StockEntity stockEntity = optionalStock.get();
                stockEntity.setUpdatedAt(LocalDateTime.now());
                StockEntity entity = stockRepository.save(stockEntity); //업데이트
                System.out.println("[Save Entity Consumer] " + entity);
            } else {
                throw new RuntimeException("Stock not found");
            }
            channel.basicAck(deliveryTag, false);  //여러줄을 할께 아니라 multiple false

        } catch (Exception e) {
            System.out.println("[Consumer Error]" + e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            } catch (IOException ex) {
                System.out.println("[Consumer Send Error]" + e.getMessage());
//                throw new RuntimeException(ex);
            }
        }
    }
}
