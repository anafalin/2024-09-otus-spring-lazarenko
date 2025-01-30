package ru.otus.hw.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDao {

    private static final String UPDATE_PRODUCT_QUERY = "UPDATE Products SET quantity = ? WHERE id=?;";

    private static final String SELECT_PRODUCTS_BY_ID_IN = "SELECT * FROM products where id in (%s) FOR UPDATE;";

    private static final Integer BATCH_SIZE = 200_000;

    private final EntityManagerFactory entityManagerFactory;

    public void updateProductQuantity(Map<Long, Integer> productIdAmountMap) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = (Session) entityManager.getDelegate();

        session.doWork(connection -> {
            try (connection;
                 Statement stmt = connection.createStatement();
                 PreparedStatement psUpdate = connection.prepareStatement(UPDATE_PRODUCT_QUERY)) {
                connection.setAutoCommit(false);

                updateBatchProduct(productIdAmountMap, connection, stmt, psUpdate);
            }
        });
    }

    private static void updateBatchProduct(Map<Long, Integer> productIdAmountMap, Connection connection,
                                           Statement stmt, PreparedStatement psUpdate) throws SQLException {
        try (ResultSet rsProducts = stmt.executeQuery(preparedSelectQueryForUpdate(productIdAmountMap))) {

            int rowCounter = 0;

            while (rsProducts.next()) {
                Long id = (Long) rsProducts.getObject("id");
                Long currentAmount = (Long) rsProducts.getObject("quantity");
                psUpdate.setLong(1, currentAmount + productIdAmountMap.get(id));
                psUpdate.setLong(2, id);
                psUpdate.addBatch();

                if (++rowCounter % BATCH_SIZE == 0) {
                    psUpdate.executeBatch();
                    psUpdate.clearBatch();
                    rowCounter = 0;
                }
            }
            psUpdate.executeBatch();
            connection.commit();
        }
    }

    private static String preparedSelectQueryForUpdate(Map<Long, Integer> productIdAmountMap) {
        return SELECT_PRODUCTS_BY_ID_IN.formatted(
                productIdAmountMap.keySet()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))
        );
    }
}