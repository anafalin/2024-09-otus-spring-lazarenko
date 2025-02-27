package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

import java.util.HashMap;
import java.util.Map;

@Component("BookHealth")
@RequiredArgsConstructor
public class BookHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        Long count = bookService.getCountBooks();
        if (count != null && count > 0) {
            Map<String, Object> details = new HashMap<>();
            details.put("message", "Books is exist and it is ok!");
            details.put("booksSize", count);
            return Health.up()
                    .withDetails(details)
                    .build();
        }
        return Health.down()
                .withDetail("message", "Books is not exist!")
                .build();
    }
}