package ru.otus.hw.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.ProductInfoNotificationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private static final String SQL_UPDATE = "UPDATE Products SET quantity = ? WHERE id=?;";

    private static final String SQL_SELECT_BY_ID_IN = "SELECT * FROM products where id in (%s) FOR UPDATE;";

    private static Integer batchSize = 200_000;

    private final ProductGateway productGateway;

    private final EntityManagerFactory entityManagerFactory;

    public void spendReceiptGoods(Map<Long, Integer> productIdAmountMap) {
        log.info("Spending receipt goods of {}", productIdAmountMap);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = (Session) entityManager.getDelegate();

        session.doWork(connection -> updateProductsQuantity(productIdAmountMap, connection));
    }

    private void updateProductsQuantity(Map<Long, Integer> productIdAmountMap, Connection connection)
            throws SQLException {
        try (Statement stmt = connection.createStatement();
             PreparedStatement psUpdate = connection.prepareStatement(SQL_UPDATE)) {
            connection.setAutoCommit(false);

            int rowCounter = 0;

            ResultSet resultSetProducts = stmt.executeQuery(preparedSelectQueryForUpdate(productIdAmountMap));

            while (resultSetProducts.next()) {
                Long id = (Long) resultSetProducts.getObject("id");
                String name = resultSetProducts.getString("name");
                Long currentAmount = (Long) resultSetProducts.getObject("quantity");
                psUpdate.setLong(1, currentAmount + productIdAmountMap.get(id));
                psUpdate.setLong(2, id);
                psUpdate.addBatch();

                executeNotifyProductArrival(currentAmount, id, name);

                if (++rowCounter % batchSize == 0) {
                    psUpdate.executeBatch();
                    psUpdate.clearBatch();
                    rowCounter = 0;
                }
            }

            psUpdate.executeBatch();
            connection.commit();
        }
    }

    private void executeNotifyProductArrival(Long currentAmount, Long id, String name) {
        if(currentAmount == 0) {
            productGateway.notifyProductArrival(new ProductInfoNotificationDto(id, name));
        }
    }

    private static String preparedSelectQueryForUpdate(Map<Long, Integer> productIdAmountMap) {
        return SQL_SELECT_BY_ID_IN
                .formatted(productIdAmountMap.keySet()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
    }
}