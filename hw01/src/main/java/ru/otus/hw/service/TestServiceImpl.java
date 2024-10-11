package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();

        for (Question question : questions) {
            printQuestionWithAnswers(question);
        }
    }

    private void printQuestionWithAnswers(Question question) {
        ioService.printLine("");
        ioService.printFormattedLine("%s%s%s", "\u001B[34m", question.text(), "\u001B[0m");

        for (int i = 0; i < question.answers().size(); i++) {
            ioService.printFormattedLine("%s) %s", i + 1, question.answers().get(i).text());
        }
    }

}