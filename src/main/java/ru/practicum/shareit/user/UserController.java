package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@RequestBody @Valid User user) {
        log.info("request to add user {}", user);

        return userService.saveUser(user);
    }

    @Validated
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable(name = "userId") @Positive Long userId, @RequestBody User user) {
        log.info("request to update user {}", user);

        return userService.updateUser(userId, user);
    }

    @Validated
    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable(name = "userId") @Positive Long userId) {
        log.info("request to get user with id {}", userId);
        return  userService.getUserById(userId);
    }

    @Validated
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(name = "userId") @Positive Long userId) {
        log.info("request to delete user with id {}", userId);
        userService.deleteUser(userId);
    }
}
