package ru.tikskit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tikskit.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
}
