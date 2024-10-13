package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {

        // Создание контекста на основе Annotation/Java конфигурирования
        ApplicationContext context = new AnnotationConfigApplicationContext(AppProperties.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}