package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.hw.service.ProductService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
@EnableScheduling
public class Application {
    private final ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

// For test start
    @Scheduled(cron = "*/10 * * * * *")
    public void scheduleProductArrival() {
        Map<Long, Integer> productIdAmountMap = generateProductIdAmountMap();
        productService.spendReceiptGoods(productIdAmountMap);
    }

    private Map<Long, Integer> generateProductIdAmountMap() {
        Map<Long, Integer> productIdAmountMap = new HashMap<>();
            int quantity = (int) (Math.random() * 100 + 1);
            long productId = (long)((Math.random() * 5) + 1);
            productIdAmountMap.put(productId, quantity);
            log.info("Поступление {} - {}", productId, quantity);

        return productIdAmountMap;
    }
}