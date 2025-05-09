package com.psjw.hellomessagequeue.step5;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final CustomExceptionHandler exceptionHandler;

    public LogController(CustomExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @GetMapping("/error")
    public ResponseEntity<String> errorAPI() {
        try {
            String value = null;
            value.getBytes();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return ResponseEntity.ok("Controller NullPointer Exception 처리");
    }

    @GetMapping("/warn")
    public ResponseEntity<String> warnAPI() {
        try {
            throw new IllegalArgumentException("Invalid argument입니다.");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return ResponseEntity.ok("Controller IllegalArgument Exception 처리");
    }

    @PostMapping("/info")
    public ResponseEntity<String> infoAPI(@RequestBody String message) {
        exceptionHandler.handleMessage(message);
        return ResponseEntity.ok("Controller Info log 발송 처리");
    }

}
