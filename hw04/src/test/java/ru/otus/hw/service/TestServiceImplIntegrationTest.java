package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplIntegrationTest {
    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private TestFileNameProvider testFileNameProvider;

    @BeforeEach
    void setUp() {
        QuestionDao questionDao = new CsvQuestionDao(testFileNameProvider);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    @DisplayName("""
            executeTestFor
            | test result contains student, rightAnswersCount is 1
            | questions list is not empty, contains one question and entered correct number of answer""")
    void executeTestFor_correctResult_questionsListNotEmpty() {
        // given
        var student = new Student("Mike", "Smith");
        var question = new Question("What is not a collection in Java?", List.of(
                new Answer("ArrayList", false),
                new Answer("HashSet", false),
                new Answer("Array", true),
                new Answer("LinkedHashSet", false)));
        var questionsFile = "correct_questions.csv";
        var inputAnswer = 3;

        // when
        when(testFileNameProvider.getTestFileName())
                .thenReturn(questionsFile);

        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);

        var result = testService.executeTestFor(student);

        // then
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStudent().firstName()).isEqualTo(student.firstName());
        assertThat(result.getAnsweredQuestions().get(0).text()).isEqualTo(question.text());
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }
}