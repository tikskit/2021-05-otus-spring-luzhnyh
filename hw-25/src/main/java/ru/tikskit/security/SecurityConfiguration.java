package ru.tikskit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final int strength = 10;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DatabaseUserDetailsService userDetailsService;


    /**
     * Роли в приложении:
     *   BOOK_MANAGER - Может только создавать и удалять книги
     *   BOOK_SUPPORTER - Может только изменять книги
     *   BOOK_REVIEWER - Может только оставлять комментарии к книгам
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // Pages access
                .authorizeRequests().antMatchers("/").permitAll() // Список книг разрешаем смотреть всем
                .and()
                .authorizeRequests().antMatchers("/addbook").hasAuthority("ROLE_BOOK_MANAGER")
                .and()
                .authorizeRequests().antMatchers("/editbook").hasAuthority("ROLE_BOOK_SUPPORTER")
                .and()

                // REST API access
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/book").permitAll()// Список книг разрешаем смотреть всем
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/book").hasAuthority("ROLE_BOOK_MANAGER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PATCH, "/api/book/*").hasAuthority("ROLE_BOOK_SUPPORTER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/book/*").hasAuthority("ROLE_BOOK_MANAGER")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/book/*/comment").hasAuthority("ROLE_BOOK_REVIEWER")
                .and()

                .authorizeRequests().antMatchers("/**").denyAll() // Закрываем доступ для всего, что не разрешили явно
                .and()
                .formLogin().usernameParameter("user").passwordParameter("pass")
                .and()
                .logout().logoutUrl("/logout");
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }

}
