package ru.otus.hw.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleanupListener implements JobExecutionListener {
    private final MongoTemplate mongoTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        mongoTemplate.dropCollection(MongoComment.class);
        mongoTemplate.dropCollection(MongoBook.class);
        mongoTemplate.dropCollection(MongoAuthor.class);
        mongoTemplate.dropCollection(MongoGenre.class);

        log.info("Database was cleanup");
    }
}