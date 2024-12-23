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
import ru.otus.hw.model.jpa.JpaComment;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.transformer.ModelTransformer;
import ru.otus.hw.writer.BufferedMongoItemWriter;

@Configuration
@RequiredArgsConstructor
public class CommentMigrationStepConfig {
    private static final int CHUNK_SIZE = 1_000;

    private static final int BATCH_SIZE = 10_000;

    private final MongoTemplate mongoTemplate;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public JpaCursorItemReader<JpaComment> commentJpaReader(EntityManagerFactory entityManagerFactory) {
        JpaCursorItemReader<JpaComment> reader = new JpaCursorItemReader<>();

        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("commentJpaItemReader");
        reader.setQueryString("SELECT c FROM JpaComment c");

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<JpaComment, MongoComment> commentTransformProcessor() {
        return ModelTransformer::commentTransform;
    }

    @Bean
    @StepScope
    public BufferedMongoItemWriter<MongoComment> commentMongoWriter() {
        return new BufferedMongoItemWriter<>(mongoTemplate, BATCH_SIZE);
    }

    @Bean
    public Step commentTransformJpaToMongoStep(JpaCursorItemReader<JpaComment> commentItemReader,
                                               ItemProcessor<JpaComment, MongoComment> commentTransformProcessor,
                                               BufferedMongoItemWriter<MongoComment> commentItemWriter) {
        return new StepBuilder("transformCommentJpaToMongo", jobRepository)
                .<JpaComment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentItemReader)
                .processor(commentTransformProcessor)
                .writer(commentItemWriter)
                .build();
    }
}