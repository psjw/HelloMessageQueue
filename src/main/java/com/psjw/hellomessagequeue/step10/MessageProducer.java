package com.psjw.hellomessagequeue.step10;

import java.time.LocalDateTime;
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
    public void sendMessage(StockEntity stockEntity, String testCase) {
        rabbitTemplate.execute(channel -> {
            try {
                channel.txSelect(); //트랜잭션 시작
                stockEntity.setProcessed(false);
                stockEntity.setCreatedAt(LocalDateTime.now());
                StockEntity stockEntitySaved = stockRepository.save(stockEntity);

                // 메시지 발행
                rabbitTemplate.convertAndSend("transactionQueue", stockEntitySaved);
                if ("fail".equals(testCase)) {
                    throw new RuntimeException("트랜잭션 작업중에 에러 발생");
                }
                channel.txCommit();
                System.out.println("트랜잭션이 정상적으로 처리 되었음~");
            } catch (Exception e) {
                System.out.println("트랜잭션 실패 : " + e.getMessage());
                channel.txRollback();
                throw new RuntimeException("트랜잭션 롤백 완료 ", e);
            } finally {
                if (channel != null) {
                    try{
                        channel.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            return null;
        });
    }

}
