package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {
    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @DisplayName(""" 
            findAll
            | correct read questions file what exists and result list contains expected question
            | questions file exists and contains one question""")
    @Test
    void findAll_correctReadFile_questionFileExistsAndNotEmpty() {
        // given
        String questionFile = "correct_questions.csv";
        var expectedQuestion = new Question("What is not a collection in Java?", List.of(
                new Answer("ArrayList", false),
                new Answer("HashSet", false),
                new Answer("Array", true),
                new Answer("LinkedHashSet", false)));

        // when
        when(testFileNameProvider.getTestFileName())
                .thenReturn(questionFile);

        var questionList = csvQuestionDao.findAll();

        // then
        assertTrue(questionList.contains(expectedQuestion),
                "Result questions list must contain expected question");
    }

    @DisplayName("""
            findAll
            | correct read questions file what exists and result list contains expected question
            | questions file exists and does not contain questions""")
    @Test
    void findAll_correctReadFile_questionFileExistsAndEmpty() {
        // given
        String questionFile = "empty_questions.csv";

        // when
        when(testFileNameProvider.getTestFileName())
                .thenReturn(questionFile);

        var questionList = csvQuestionDao.findAll();

        // then
        assertTrue(questionList.isEmpty(), "Result questions list must be empty");
    }

    @DisplayName(""" 
            findAll
            | NullPointerException with message
            | questions file not exists and contains one question""")
    @Test
    void findAll_nullPointerException_incorrectQuestionFile() {
        // given
        String expectedMessage = "Error of read question";
        String questionFile = "not_exist_file_questions.csv";

        // when
        when(testFileNameProvider.getTestFileName())
                .thenReturn(questionFile);

        var expected = assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());

        // then
        assertEquals(expectedMessage, expected.getMessage(),
                "Exception message must equals expected message");
        assertEquals(NullPointerException.class, expected.getCause().getClass(),
                "Throwable class must be of NullPointerException");
    }
}