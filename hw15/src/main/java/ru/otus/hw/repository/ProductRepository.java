package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.dto.ProductInfoNotificationDto;
import ru.otus.hw.entity.Product;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductInfoNotificationDto> findAllByIdInAndQuantityEquals(Collection<Long> ids, Long quantity);
}