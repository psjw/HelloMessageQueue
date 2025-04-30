package com.psjw.hellomessagequeue.step2.step1;

import org.springframework.stereotype.Component;

@Component
public class WorkQueueConsumer {

    public void workQueueTask(String message) {
        String[] messagePart = message.split("\\|");
        String originalMessage = messagePart[0];
        int duration = Integer.parseInt(messagePart[1]);

        System.out.println(
                "# Received message: " + originalMessage + " (duration: " + duration + " ms)");
        try {
            System.out.println("now...sleep time " + duration + "ms");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[X] Completed: " + originalMessage);
    }
}
