package ru.tikskit.actuators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.tikskit.dao.BookDao;
import ru.tikskit.service.DBBookService;

@Component
@AllArgsConstructor
public class BooksHealthIndicator implements HealthIndicator {

    private final DBBookService bookService;

    @Override
    public Health health() {
        if (bookService.getCount() == 0) {
            return Health
                    .down()
                    .status(Status.DOWN)
                    .withDetail("message", "В библиотеке нет ни одной книги, что-то не так!")
                    .build();
        } else {
            return Health
                    .up()
                    .withDetail("message", "Книги на месте")
                    .build();
        }
    }
}
