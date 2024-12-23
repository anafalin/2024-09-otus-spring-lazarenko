package ru.otus.hw.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

public class BufferedMongoItemWriter<T> implements ItemWriter<T> {
    private final MongoTemplate mongoTemplate;

    private final List<T> buffer;

    private final int batchSize;

    public BufferedMongoItemWriter(MongoTemplate mongoTemplate, int batchSize) {
        this.mongoTemplate = mongoTemplate;
        this.batchSize = batchSize;
        this.buffer = new ArrayList<>(batchSize);
    }

    public void flush() {
        if (!buffer.isEmpty()) {
            mongoTemplate.insertAll(buffer);
            buffer.clear();
        }
    }

    public void close() {
        flush();
    }

    @Override
    public void write(Chunk<? extends T> chunk) {
        buffer.addAll(chunk.getItems());

        if (buffer.size() >= batchSize) {
            flush();
        }
    }
}