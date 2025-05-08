package ru.webrise.subscriptionsmanager.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.webrise.subscriptionsmanager.infrastructure.exceptions.ApplicationException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException: {}", exception.getMessage());
        final List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        final ErrorMessage message = new ErrorMessage("100", errors.toString());

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("ConstraintViolationException: {}", exception.getMessage());
        final ErrorMessage message = new ErrorMessage("101", exception.getMessage());

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorMessage> handleApplicationException(ApplicationException exception) {
        log.error("ApplicationException: {}", exception.getMessage());
        final ErrorMessage message = new ErrorMessage("102", exception.getMessage());

        return ResponseEntity.status(exception.getHttpStatus()).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        log.error("Exception: {}", exception.getMessage());
        final ErrorMessage message = new ErrorMessage("9999", exception.getMessage());

        return ResponseEntity.internalServerError().body(message);
    }

    public record ErrorMessage(
            @JsonProperty("errorCode") String errorCode,
            @JsonProperty("message") String message) {}
}
