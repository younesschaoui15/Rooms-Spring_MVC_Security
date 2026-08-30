package com.chaoui.rooms.configurations.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleException(AuthenticationException e) {
        e.printStackTrace();

        var httpStatus = HttpStatus.UNAUTHORIZED;

        Map<String, Object> error = Map.of(
            "Datetime", Instant.now(),
            "Status", httpStatus.value(),
            "Message", "Invalid username or password"
        );

        return ResponseEntity.status(httpStatus).body(error);
    }
}
