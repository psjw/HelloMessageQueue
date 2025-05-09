package com.psjw.hellomessagequeue.step11;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageProducer {

    private final StockRepository stockRepository;
    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(StockRepository stockRepository, RabbitTemplate rabbitTemplate) {
        this.stockRepository = stockRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Transactional
    public void sendMessage(StockEntity stockEntity, boolean testCase) {
        stockEntity.setProcessed(false);
        stockEntity.setCreatedAt(LocalDateTime.now());
        StockEntity stockEntitySaved = stockRepository.save(stockEntity);

        System.out.println("[producer entity] : " + stockEntity);

        if (stockEntity.getUserId() == null || stockEntity.getUserId().isEmpty()) {
            throw new RuntimeException("User id is required");
        }

        try {
            //메시지를 rabbitMQ에 전송
            CorrelationData correlationData = new CorrelationData(
                    stockEntity.getUserId().toString());
            //만약 ID가 없으면 UUID.randomUUID() 발행하여 주입해서 트래킹
            rabbitTemplate.convertAndSend(
                    testCase ? "nonExistentExchange" : RabbitMQConfig.EXCHANGE_NAME,
                    testCase ? "invalidRoutingKey" : RabbitMQConfig.ROUTING_KEY,
                    stockEntity,
                    correlationData
            );

            if (correlationData.getFuture().get(5, TimeUnit.SECONDS).isAck()) {
                System.out.println("[producer correlationData] 성공 " + stockEntity);
                stockEntity.setProcessed(true);
                stockRepository.save(stockEntity);
            } else {
                throw new RuntimeException("# confirm 실패 - 롤백");
            }
        } catch (Exception e) {
            System.out.println("[producer exception fail] : " + e);
            throw new RuntimeException(e);
        }
    }

}
