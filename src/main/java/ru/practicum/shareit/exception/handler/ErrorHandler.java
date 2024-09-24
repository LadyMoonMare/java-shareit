package ru.practicum.shareit.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    //exception for two same emails
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleSameEmail(final RuntimeException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }

    //exception for non-existing entity
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchUser(final RuntimeException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }

    //exception for forbidden access to item for not owner
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleNotOwnerAccess(final RuntimeException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}
