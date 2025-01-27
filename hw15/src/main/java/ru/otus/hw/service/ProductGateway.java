package ru.otus.hw.service;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.dto.ProductInfoNotificationDto;

@MessagingGateway
public interface ProductGateway {
    @Gateway(requestChannel = "productArrivalChannel")
    void notifyProductArrival(ProductInfoNotificationDto product);
}