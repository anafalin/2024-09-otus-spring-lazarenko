package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final ResultService resultService;

    private final StudentContextService studentContextService;

    @Override
    public void run() {
        var testResult = testService.executeTestFor(studentContextService.getUser());
        resultService.showResult(testResult);
    }
}