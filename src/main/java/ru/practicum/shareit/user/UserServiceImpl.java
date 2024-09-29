package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            return UserMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("this email is already exist");
        }
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        log.info("validation in service layer for updating user: {}, id {}", user, userId);
        User userToUpdate = findUserById(userId);
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }

        try {
            return UserMapper.toUserDto(userRepository.save(userToUpdate));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("this email is already exist");
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(findUserById(id));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("validation in service layer for delete user with id {}", id);
        User user = findUserById(id);

        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", id);
            return new NotFoundException("User is not found");
        });
    }
}