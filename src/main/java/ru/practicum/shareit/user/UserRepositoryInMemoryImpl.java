package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.IdGenerator;

import java.util.*;

@Slf4j
@Component
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public User save(User user) {
        log.info("attempt to add in memory user: {}", user);
        user.setId(idGenerator.getId());
        log.info("id from generator = {}, userId = {}",idGenerator.getId(), user.getId());
        idGenerator.reloadId();

        users.put(user.getId(),user);
        log.info("user successfully added to memory: {}", users.get(user.getId()));
        return user;
    }

    @Override
    public List<User> getAll() {
        log.info("return all users from memory");
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User update(Long id, User user) {
        log.info("updating user with id = {} from user = {}; to user {}", id, users.get(id),user);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public void delete(Long id) {
        log.info("deleting user with id = {}", id);
        users.remove(id);
    }
}
