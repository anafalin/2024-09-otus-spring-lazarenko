package ru.otus.hw.step;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.jpa.JpaBook;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.transformer.ModelTransformer;
import ru.otus.hw.writer.BufferedMongoItemWriter;

@Configuration
@RequiredArgsConstructor
public class BookMigrationStepConfig {
    private static final int CHUNK_SIZE = 50_000;

    private static final int BATCH_SIZE = 100_000;

    private final MongoTemplate mongoTemplate;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public JpaCursorItemReader<JpaBook> bookJpaReader(EntityManagerFactory entityManagerFactory) {

        JpaCursorItemReader<JpaBook> reader = new JpaCursorItemReader<>();

        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("bookJpaItemReader");
        reader.setQueryString("SELECT b FROM JpaBook b LEFT JOIN FETCH b.genres");
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<JpaBook, MongoBook> bookTransformProcessor() {
        return ModelTransformer::bookTransform;
    }

    @Bean
    @StepScope
    public BufferedMongoItemWriter<MongoBook> bookMongoWriter() {
        return new BufferedMongoItemWriter<>(
                mongoTemplate, BATCH_SIZE
        );
    }

    @Bean
    public Step bookTransformJpaToMongoStep(JpaCursorItemReader<JpaBook> bookItemReader,
                                            ItemProcessor<JpaBook, MongoBook> bookTransformProcessor,
                                            BufferedMongoItemWriter<MongoBook> bookMongoWriter) {
        return new StepBuilder("transformBookJpaToMongo", jobRepository)
                .<JpaBook, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookItemReader)
                .processor(bookTransformProcessor)
                .writer(bookMongoWriter)
                .build();
    }
}
