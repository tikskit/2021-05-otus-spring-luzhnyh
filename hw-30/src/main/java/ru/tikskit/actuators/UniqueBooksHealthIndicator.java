package ru.tikskit.actuators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UniqueBooksHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return null;
    }
}
