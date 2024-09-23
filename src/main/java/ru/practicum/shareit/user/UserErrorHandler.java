package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Map;

@RestControllerAdvice
public class UserErrorHandler {

    //exception for two same emails
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleSameEmail(final RuntimeException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }

    //exception for non-existing user
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchUser(final RuntimeException e) {
        return Map.of(
                "errorMessage", e.getMessage()
        );
    }
}
