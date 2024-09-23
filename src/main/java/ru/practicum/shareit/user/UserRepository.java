package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> getAll();

    Optional<User> findUserById(Long id);

    User update(Long id, User user);

    void delete(Long id);
}

