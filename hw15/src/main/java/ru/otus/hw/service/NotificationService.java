package ru.otus.hw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.EmailNotification;
import ru.otus.hw.entity.Subscription;

@Service
@Slf4j
public class NotificationService {

    @Value("${spring.mail.username}")
    private String usernameFrom;

    @Value("${app.product.path.url}")
    private String productPathUrl;

    public EmailNotification createNotification(Subscription subscription) {
        String message = String.format("""
                        Hello!
                        The 'product' is back in stock!
                        To go to the product page, click on the following link - %s%s"
                        """,
                productPathUrl,
                subscription.getProductId());

        EmailNotification emailNotification = EmailNotification.builder()
                .usernameFrom(usernameFrom)
                .emailTo(subscription.getClientEmail())
                .subject("Product %s is available again".formatted(subscription.getProductName()))
                .text(message)
                .build();
        log.info("Created email notification: {}", emailNotification);

        return emailNotification;
    }
}