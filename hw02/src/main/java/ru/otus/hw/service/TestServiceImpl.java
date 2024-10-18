package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private static final int MAX_COUNT_QUESTIONS_FOR_TEST = 5;

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        var questions = getRandomQuestions();
        var testResult = new TestResult(student);

        for (var question : questions) {
            printQuestionWithAnswers(question);
            var isAnswerValid = readAnswerQuestion(question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private Collection<Question> getRandomQuestions() {
        List<Question> allQuestions = questionDao.findAll();
        Set<Question> randomQuestions = new HashSet<>();
        Random random = new Random();

        if (allQuestions.size() < MAX_COUNT_QUESTIONS_FOR_TEST) {
            return allQuestions;
        }

        while (randomQuestions.size() < MAX_COUNT_QUESTIONS_FOR_TEST) {
            int randomIndex = random.nextInt(allQuestions.size());
            randomQuestions.add(allQuestions.get(randomIndex));
        }
        return randomQuestions;
    }


    private void printQuestionWithAnswers(Question question) {
        ioService.printFormattedLine("%s%s%s", "\u001B[34m", question.text(), "\u001B[0m");

        for (int i = 0; i < question.answers().size(); i++) {
            ioService.printFormattedLine("%s) %s", i + 1, question.answers().get(i).text());
        }
    }

    private boolean readAnswerQuestion(Question question) {
        var countAnswers = question.answers().size();
        int numberAnswer = ioService.readIntForRangeWithPrompt(1, countAnswers,
                "Number of your answer must be between 1 and %s".formatted(countAnswers),
                "Incorrect entered number of your answer");

        ioService.printLine("");
        ioService.printFormattedLine("%s%s%s", "\u001B[34m", "Input number of your answer", "\u001B[0m");
        return isAnswerCorrect(question, numberAnswer);
    }

    private boolean isAnswerCorrect(Question question, int numberAnswer) {
        for (int i = 0; i < question.answers().size(); i++) {
            if (question.answers().get(i).isCorrect()) {
                return numberAnswer == ++i;
            }
        }
        return false;
    }
}