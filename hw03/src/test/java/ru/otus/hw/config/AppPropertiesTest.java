package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableConfigurationProperties(value = AppProperties.class)
public class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Test
    @DisplayName("should get correct 'RightAnswersCountToPass'")
    public void testGetRightAnswersCountToPass() {
        var result = appProperties.getRightAnswersCountToPass();

        assertEquals(4, result);
    }

    @Test
    @DisplayName("should get correct 'Locale'")
    public void testGetLocale() {
        var result = appProperties.getLocale();

        assertEquals(Locale.forLanguageTag("ru-RU"), result);
    }

    @Test
    @DisplayName("should get correct 'testFileName'")
    public void testGetTestFileName() {
        String expected = "questions_ru.csv";

        var result = appProperties.getTestFileName();

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("should get correct 'fileNameByLocaleTag'")
    public void testFileNameByLocaleTag() {
        Map<String, Field> result = Arrays.stream(appProperties.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, field -> field));

        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertTrue(result.containsKey("fileNameByLocaleTag"))
        );
    }
}