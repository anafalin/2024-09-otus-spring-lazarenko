package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.ProductInfoNotificationDto;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductNotifier {

    private final ProductGateway productGateway;

    public void notifyProductArrival(List<ProductInfoNotificationDto> products) {
        products.forEach(productGateway::notifyProductArrival);
    }
}