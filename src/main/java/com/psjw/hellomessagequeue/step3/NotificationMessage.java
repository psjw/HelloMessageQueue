package com.psjw.hellomessagequeue.step3;

public class NotificationMessage {
    private String message;

    // 기본 생성자 (필수)
    public NotificationMessage() {
    }

    // 선택
    public NotificationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
