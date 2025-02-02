package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.EmailNotification;
import ru.otus.hw.dto.ProductInfoNotificationDto;
import ru.otus.hw.entity.Subscription;
import ru.otus.hw.service.MailSenderService;
import ru.otus.hw.service.NotificationService;
import ru.otus.hw.service.ProductGateway;
import ru.otus.hw.service.SubscriptionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringIntegrationTest
public class IntegrationConfigTest {

    @Autowired
    private ProductGateway productGateway;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private MailSenderService mailSenderService;

    @BeforeEach
    public void setup() {
        Subscription subscription = new Subscription();
        subscription.setProductId(1L);
        subscription.setClientEmail("client@example.com");
        subscription.setProductName("Test Product");

        EmailNotification emailNotification = EmailNotification.builder()
                .usernameFrom("test@example.com")
                .emailTo("client@example.com")
                .subject("Product Test Product is available again")
                .text("Test message")
                .build();

        when(subscriptionService.findAllByProductId(anyLong()))
                .thenReturn(List.of(subscription));

        when(notificationService.createNotification(any(Subscription.class)))
                .thenReturn(emailNotification);
    }

    @Test
    public void testProductArrivalFlow() {
        ProductInfoNotificationDto dto = new ProductInfoNotificationDto(1L, "Test Product");
        productGateway.notifyProductArrival(dto);

        verify(subscriptionService, times(1)).findAllByProductId(1L);
        verify(notificationService, times(1)).createNotification(any(Subscription.class));
        verify(mailSenderService, times(1)).sendMail(any(EmailNotification.class));
    }

}