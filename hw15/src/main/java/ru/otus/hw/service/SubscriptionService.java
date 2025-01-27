package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.entity.Subscription;
import ru.otus.hw.repository.SubscriptionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public List<Subscription> findAllByProductId(Long id) {
        log.info("findAllByProductId {}", id);
        return subscriptionRepository.findAllByProductId(id);
    }
}
