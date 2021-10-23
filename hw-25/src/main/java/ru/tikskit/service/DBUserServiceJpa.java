package ru.tikskit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.User;
import ru.tikskit.repository.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class DBUserServiceJpa implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(DBUserServiceJpa.class);

    private final UserRepository userRepository;

    public DBUserServiceJpa(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUser(String login) {
        Optional<User> userByLogin = Optional.of(userRepository.findUserByLogin(login));
        logger.info("User got from db: {}", userByLogin.orElse(null));
        return userByLogin;
    }
}
