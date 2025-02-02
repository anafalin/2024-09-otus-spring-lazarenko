package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.dto.ProductInfoNotificationDto;
import ru.otus.hw.entity.Subscription;
import ru.otus.hw.service.NotificationService;
import ru.otus.hw.service.SubscriptionService;

@Configuration
@EnableIntegration
@RequiredArgsConstructor
public class IntegrationConfig {
    private final SubscriptionService subscriptionService;

    private final NotificationService notificationService;

    @Bean
    public IntegrationFlow productArrivalFlow() {
        return IntegrationFlow
                .from("productArrivalChannel")
                .transform(ProductInfoNotificationDto.class, ProductInfoNotificationDto::id)
                .transform(subscriptionService::findAllByProductId)
                .split()
                .transform(Subscription.class, notificationService::createNotification)
                .handle("mailSenderService", "sendMail")
                .get();
    }

    @Bean
    public MessageChannel productArrivalChannel() {
        return new DirectChannel();
    }
}