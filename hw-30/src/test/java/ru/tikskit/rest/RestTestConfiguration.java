package ru.tikskit.rest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.tikskit"})
@EntityScan(basePackages = {"ru.tikskit.domain"})
@EnableJpaRepositories(basePackages = "ru.tikskit.dao")
public class RestTestConfiguration {
}
