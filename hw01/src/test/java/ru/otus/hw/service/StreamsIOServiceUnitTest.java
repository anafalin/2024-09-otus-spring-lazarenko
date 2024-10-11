package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceUnitTest {
    @InjectMocks
    private StreamsIOService streamsIOService;

    @Mock
    private PrintStream printStream;

    @Test
    @DisplayName("should 'println' happened once when invoke method 'printLine'")
    void printLine() {
        // GIVEN
        String testString = "Test string";

        // WHEN
        streamsIOService.printLine(testString);

        // THEN
        verify(printStream).println(testString);
        verifyNoMoreInteractions(printStream);
    }

    @Test
    @DisplayName("should 'printf' happened once when invoke method 'printFormattedLine'")
    void printFormattedLine() {
        // GIVEN
        String testString = "Test string";
        String resultString = testString.concat("%n");

        // WHEN
        streamsIOService.printFormattedLine(testString);

        // THEN
        verify(printStream).printf(resultString);
    }
}