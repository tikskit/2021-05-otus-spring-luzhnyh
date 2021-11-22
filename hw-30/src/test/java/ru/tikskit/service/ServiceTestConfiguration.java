package ru.tikskit.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"ru.tikskit.service", "ru.tikskit.dao"})
@EntityScan(basePackages = {"ru.tikskit.domain"})
public class ServiceTestConfiguration {
}
