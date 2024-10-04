package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;

import static org.mockito.Mockito.verify;

class StreamsIOServiceUnitTest {

    private StreamsIOService underTest;
    private PrintStream printStream;

    @BeforeEach
    void setUp() {
        printStream = Mockito.mock(PrintStream.class);
        underTest = new StreamsIOService(printStream);
    }

    @Test
    @DisplayName("should 'println' happened once when invoke method 'printLine'")
    void printLine() {
        // GIVEN
        String testString = "Test string";

        // WHEN
        underTest.printLine(testString);

        // THEN
        verify(printStream).println(testString);
    }

    @Test
    @DisplayName("should 'printf' happened once when invoke method 'printFormattedLine'")
    void printFormattedLine() {
        // GIVEN
        String testString = "Test string";
        String resultString = testString.concat("%n");

        // WHEN
        underTest.printFormattedLine(testString);

        // THEN
        verify(printStream).printf(resultString);
    }
}