package com.chaoui.rooms.configurations.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
@Slf4j(topic = "GlobalExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleException(AuthenticationException e) {
        log.error("Authentication Exception: {}", e.getMessage());
        e.printStackTrace();

        var httpStatus = HttpStatus.UNAUTHORIZED;

        Map<String, Object> error = Map.of(
            "Datetime", Instant.now(),
            "Status", httpStatus.value(),
            "Message", "Invalid username or password"
        );

        return ResponseEntity.status(httpStatus).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleException(EntityNotFoundException e) {
        log.error("Entity Not Found Exception: {}", e.getMessage());
        e.printStackTrace();

        var httpStatus = HttpStatus.NOT_FOUND;

        Map<String, Object> error = Map.of(
            "Datetime", Instant.now(),
            "Status", httpStatus.value(),
            "Message", "Entity not found"
        );

        return ResponseEntity.status(httpStatus).body(error);
    }
}
