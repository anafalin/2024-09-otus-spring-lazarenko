package ru.otus.hw.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import ru.otus.hw.listener.DatabaseCleanupListener;

@Configuration
@RequiredArgsConstructor
public class MigrationJobConfig {
    private static final String MIGRATION_LIBRARY_JOB_NAME = "migrationLibraryJpaToMongoJob";

    private final JobRepository jobRepository;

    private final DatabaseCleanupListener databaseCleanupListener;

    @Bean
    public Flow splitMigrationAuthorsAndGenresJpaToMongo(Step authorTransformJpaToMongoStep,
                                                         Step genreTransformJpaToMongoStep) {
        return new FlowBuilder<Flow>("splitFlow")
                .split(new SimpleAsyncTaskExecutor("task_executor_async_migration"))
                .add(migrationGenres(genreTransformJpaToMongoStep), migrationAuthors(authorTransformJpaToMongoStep))
                .build();
    }

    @Bean
    public Flow migrationGenres(Step genreTransformJpaToMongoStep) {
        return new FlowBuilder<Flow>("flow2")
                .start(genreTransformJpaToMongoStep)
                .build();
    }

    @Bean
    public Flow migrationAuthors(Step authorTransformJpaToMongoStep) {
        return new FlowBuilder<Flow>("flow2")
                .start(authorTransformJpaToMongoStep)
                .build();
    }

    @Bean
    public Job migrationLibrary(Step authorTransformJpaToMongoStep,
                                Step genreTransformJpaToMongoStep,
                                Step bookTransformJpaToMongoStep,
                                Step commentTransformJpaToMongoStep) {
        return new JobBuilder(MIGRATION_LIBRARY_JOB_NAME, jobRepository)
                .listener(databaseCleanupListener)
                .incrementer(new RunIdIncrementer())
                .start(splitMigrationAuthorsAndGenresJpaToMongo(
                        authorTransformJpaToMongoStep,
                        genreTransformJpaToMongoStep)
                )
                .next(bookTransformJpaToMongoStep)
                .next(commentTransformJpaToMongoStep)
                .end()
                .build();
    }
}