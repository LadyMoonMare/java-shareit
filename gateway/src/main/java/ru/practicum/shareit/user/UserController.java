package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addNewUser(@RequestBody @Valid UserDto user) {
        log.info("creating user {}", user);

        return userClient.addUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(name = "userId") @Positive Long userId,
                                             @RequestBody UserDto user) {
        log.info("validating params before pathing");
        if ((user.getEmail() != null) &&
                (user.getEmail().isEmpty() || user.getEmail().isBlank()
                        || !user.getEmail().contains("@"))) {
            log.warn("invalid email for patching: {}", user.getEmail());
            throw new InvalidDataException("invalid email");
        }
        if ((user.getName() != null) && (user.getName().isEmpty() || user.getName().isBlank())) {
            log.warn("invalid name for patching: {}", user.getName());
            throw new InvalidDataException("invalid name");
        }

        log.info("patching user {}",userId);
        return userClient.updateUser(userId, user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable(name = "userId") @Positive Long userId) {
        log.info("getting user with id {}", userId);
        return  userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable(name = "userId") @Positive Long userId) {
        log.info("deleting user with id {}", userId);
        return userClient.deleteUser(userId);
    }
}
