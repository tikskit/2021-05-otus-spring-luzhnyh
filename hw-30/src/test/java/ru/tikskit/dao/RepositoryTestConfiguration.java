package ru.tikskit.dao;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"ru.tikskit.dao"})
@EntityScan(basePackages = {"ru.tikskit.domain"})
public class RepositoryTestConfiguration {
}
