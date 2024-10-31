package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class TestServiceImplTest {
    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

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
        var inputAnswer = 3;

        // when
        when(questionDao.findAll())
                .thenReturn(List.of(question));

        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(inputAnswer);

        var result = testService.executeTestFor(student);

        // then
        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getStudent().firstName()).isEqualTo(student.firstName());
        assertThat(result.getAnsweredQuestions()).contains(question);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }
}