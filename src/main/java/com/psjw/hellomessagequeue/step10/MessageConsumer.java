package com.psjw.hellomessagequeue.step10;

import java.time.LocalDateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
public class MessageConsumer {

    private final StockRepository stockRepository;

    public MessageConsumer(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @RabbitListener(queues = "transactionQueue")
    public void receiveMessage(StockEntity stockEntity) {
        System.out.println("# received message = " + stockEntity);

        try {
            stockEntity.setProcessed(true);
            stockEntity.setUpdatedAt(LocalDateTime.now());
//            stockRepository.save(stockEntity);
            System.out.println("# StockEntity 저장 완료");
        } catch (Exception e) {
            System.out.println("# Entity 수정 에러 " + e.getMessage());
            throw e; // todo 메시지를 데드레터 큐에 집어 넣는다.
        }
    }
}
