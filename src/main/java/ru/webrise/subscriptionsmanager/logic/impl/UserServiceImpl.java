package ru.webrise.subscriptionsmanager.logic.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webrise.subscriptionsmanager.infrastructure.controller.UserController;
import ru.webrise.subscriptionsmanager.infrastructure.data.Users;
import ru.webrise.subscriptionsmanager.infrastructure.data.repository.UserRepository;
import ru.webrise.subscriptionsmanager.infrastructure.exceptions.ApplicationException;
import ru.webrise.subscriptionsmanager.logic.UserService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserController.UserDto getUserInfo(UUID userId) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            final Users userEntity = userOpt.get();

            return UserController.UserDto.builder()
                    .firstName(userEntity.getFirstName())
                    .lastName(userEntity.getLastName())
                    .phoneNumber(userEntity.getPhoneNumber())
                    .build();
        }
        log.warn("There is no user record with id = {}", userId);

        return null;
    }

    @Override
    @Transactional
    public UserController.UserDto saveUser(UserController.UserDto request) {
        final Users userEntity = Users.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .build();

        final Users savedUser = userRepository.save(userEntity);
        log.info("Created user record with id: {}", savedUser.getId());

        return UserController.UserDto.builder()
                .user_id(savedUser.getId().toString())
                .build();
    }

    @Override
    @Transactional
    public void updateUser(UUID userId, UserController.UserDto request) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            log.warn("There is no user record with id = {}", userId);
            throw new ApplicationException("Не найден пользователь с id = %s".formatted(userId));
        }

        final Users userEntity = userOpt.get();

        if (Objects.nonNull(request.firstName())) {
            userEntity.setFirstName(request.firstName());
        }
        if (Objects.nonNull(request.lastName())) {
            userEntity.setLastName(request.lastName());
        }
        if (Objects.nonNull(request.phoneNumber())) {
            userEntity.setPhoneNumber(request.phoneNumber());
        }

        userRepository.save(userEntity);
        log.info("Updated user record with id: {}", userEntity.getId());
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        final Optional<Users> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            log.warn("There is no user record with id = {}", userId);
            throw new ApplicationException("Не найден пользователь с id = %s".formatted(userId));
        }

        userRepository.deleteById(userId);
        log.info("Deleted user record with id: {}", userId);
    }
}
