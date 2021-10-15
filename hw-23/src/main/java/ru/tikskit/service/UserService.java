package ru.tikskit.service;

import ru.tikskit.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(String login);
}
