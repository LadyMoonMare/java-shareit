package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserDto saveUser(User user);

    UserDto updateUser(Long userId, User user);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);

}
