package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.ProductInfoNotificationDto;
import ru.otus.hw.repository.ProductDao;
import ru.otus.hw.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductNotifier productNotifier;

    private final ProductRepository productRepository;

    private final ProductDao productDao;

    public void spendReceiptGoods(Map<Long, Integer> productIdAmountMap) {
        log.info("Spending receipt goods of {}", productIdAmountMap);

        Set<Long> ids = productIdAmountMap.keySet();
        List<ProductInfoNotificationDto> productForNotify = productRepository.findAllByIdInAndQuantityEquals(ids, 0L);

        productDao.updateProductQuantity(productIdAmountMap);

        productNotifier.notifyProductArrival(productForNotify);
    }
}