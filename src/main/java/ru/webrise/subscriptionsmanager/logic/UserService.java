package ru.webrise.subscriptionsmanager.logic;

import ru.webrise.subscriptionsmanager.infrastructure.controller.UserController;

import java.util.UUID;

public interface UserService {

    UserController.UserDto getUserInfo(UUID userId);

    UserController.UserDto saveUser(UserController.UserDto request);

    void updateUser(UUID userId, UserController.UserDto request);

    void deleteUser(UUID userId);
}
