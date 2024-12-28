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
import ru.otus.hw.model.jpa.JpaGenre;
import ru.otus.hw.model.mongo.MongoGenre;
import ru.otus.hw.transformer.ModelTransformer;

@Configuration
@RequiredArgsConstructor
public class GenreMigrationStepConfig {
    private static final int CHUNK_SIZE = 100;

    private final MongoTemplate mongoTemplate;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public JpaCursorItemReader<JpaGenre> genreJpaReader(EntityManagerFactory entityManagerFactory) {
        JpaCursorItemReader<JpaGenre> reader = new JpaCursorItemReader<>();

        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("genreJpaItemReader");
        reader.setQueryString("SELECT g FROM JpaGenre g");

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<JpaGenre, MongoGenre> genreTransformProcessor() {
        return ModelTransformer::genreTransform;
    }

    @Bean
    @StepScope
    public MongoItemWriter<MongoGenre> genreMongoWriter() {
        MongoItemWriter<MongoGenre> writer = new MongoItemWriter<>();

        writer.setCollection("genres");
        writer.setTemplate(mongoTemplate);

        return writer;
    }

    @Bean
    public Step genreTransformJpaToMongoStep(JpaCursorItemReader<JpaGenre> genreItemReader,
                                             ItemProcessor<JpaGenre, MongoGenre> genreTransformProcessor,
                                             MongoItemWriter<MongoGenre> genreItemWriter) {
        return new StepBuilder("transformGenreJpaToMongo", jobRepository)
                .<JpaGenre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreItemReader)
                .processor(genreTransformProcessor)
                .writer(genreItemWriter)
                .build();
    }
}
