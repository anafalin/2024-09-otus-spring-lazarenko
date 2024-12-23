package ru.otus.hw.step;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.model.jpa.JpaAuthor;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.transformer.ModelTransformer;

@Configuration
@RequiredArgsConstructor
public class AuthorMigrationStepConfig {
    private static final int CHUNK_SIZE = 100;

    private final MongoTemplate mongoTemplate;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public JpaCursorItemReader<JpaAuthor> authorJpaReader(EntityManagerFactory entityManagerFactory) {
        JpaCursorItemReader<JpaAuthor> reader = new JpaCursorItemReader<>();

        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("authorJpaItemReader");
        reader.setQueryString("SELECT a FROM JpaAuthor a");

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<JpaAuthor, MongoAuthor> authorTransformProcessor() {
        return ModelTransformer::authorTransform;
    }

    @Bean
    @StepScope
    public MongoItemWriter<MongoAuthor> authorMongoWriter() {
        MongoItemWriter<MongoAuthor> writer = new MongoItemWriter<>();

        writer.setCollection("authors");
        writer.setTemplate(mongoTemplate);

        return writer;
    }

    @Bean
    public Step authorTransformJpaToMongoStep(JpaCursorItemReader<JpaAuthor> authorItemReader,
                                              ItemProcessor<JpaAuthor, MongoAuthor> authorTransformProcessor,
                                              MongoItemWriter<MongoAuthor> authorItemWriter) {
        return new StepBuilder("transformAuthorJpaToMongo", jobRepository)
                .<JpaAuthor, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorItemReader)
                .processor(authorTransformProcessor)
                .writer(authorItemWriter)
                .build();
    }
}
