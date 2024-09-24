package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@Slf4j
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(User user) {
        log.info("validation in service layer for adding user: {}", user);
        validateEmail(user);

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        log.info("validation in service layer for updating user: {}, id {}", user, userId);
        User userToUpdate = findUserById(userId);
        if (user.getEmail() != null) {
            validateEmail(user);
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }

        return UserMapper.toUserDto(userRepository.update(userId, userToUpdate));
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(findUserById(id));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("validation in service layer for delete user with id {}", id);
        findUserById(id);

        userRepository.delete(id);
    }

    private void validateEmail(User user) {
        userRepository.getAll().forEach(u -> {
                    if (u.getEmail().equals(user.getEmail())) {
                        log.warn("409 conflict: user with email {} is already exist - {}", user.getEmail(), u);
                        throw new ConflictException("user with such email already exist");
                    }
                }
        );
    }

    private User findUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", id);
            return new NotFoundException("User is not found");
        });
    }
}