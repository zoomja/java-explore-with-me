package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "BAD_REQUEST");
        error.put("reason", "Incorrectly made request.");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());
        return error;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleForbiddenException(ForbiddenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("status", "FORBIDDEN");
        error.put("reason", "For the requested operation the conditions are not met.");
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now().toString());
        return error;
    }
}
