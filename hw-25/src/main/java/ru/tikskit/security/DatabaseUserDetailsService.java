package ru.tikskit.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tikskit.domain.User;
import ru.tikskit.service.UserService;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public DatabaseUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with name %s not found", username)));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPass(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
