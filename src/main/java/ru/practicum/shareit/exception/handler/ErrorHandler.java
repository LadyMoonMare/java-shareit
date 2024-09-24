package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    //exception for two same emails
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleSameEmail(final RuntimeException e) {
        log.warn("Error {}, message {}",HttpStatus.CONFLICT, e.getMessage());
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }

    //exception for non-existing entity
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchUser(final RuntimeException e) {
        log.warn("Error {}, message {}",HttpStatus.NOT_FOUND, e.getMessage());
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }

    //exception for forbidden access to item for not owner
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleNotOwnerAccess(final RuntimeException e) {
        log.warn("Error {}, message {}",HttpStatus.FORBIDDEN, e.getMessage());
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}
