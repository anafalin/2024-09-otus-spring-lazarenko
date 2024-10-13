package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StreamsIOServiceUnitTest {
    @InjectMocks
    private StreamsIOService streamsIOService;

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    @Test
    @DisplayName("should 'println' happened once when invoke method 'printLine'")
    void printLine() {
        // GIVEN
        String testString = "Test string";

        // EXECUTE
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

        // EXECUTE
        streamsIOService.printFormattedLine(testString);

        // THEN
        verify(printStream).printf(resultString);
        verifyNoMoreInteractions(printStream);
    }

    @Test
    @DisplayName("should return string what entered when invoke method 'readStringWithPrompt'")
    void readStringWithPrompt() {
        // GIVEN
        String prompt = "Some prompt";
        String inputString = "Some input string";

        // PREPARE
        refreshInputStream(inputString);

        // EXECUTE
        String result = streamsIOService.readStringWithPrompt(prompt);

        // THEN
        assertThat(inputString).isEqualTo(result);
    }

    @Test
    @DisplayName("should return string what entered when invoke method 'readString'")
    void readString() {
        // GIVEN
        String inputString = "Some input string";

        // PREPARE
        refreshInputStream(inputString);

        // EXECUTE
        String result = streamsIOService.readString();

        // THEN
        assertThat(inputString).isEqualTo(result);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRange_correctResultInt_enteredCorrectString() {
        // PREPARE
        String inputString = "2";
        refreshInputStream(inputString);

        // GIVEN
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(inputString);

        // EXECUTE
        int result = streamsIOService.readIntForRange(min, max, errorMessage);

        // THEN
        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRange_illegalArgumentExceptionWithMessage_enteredIncorrectStringMaxAttempts() {
        // PREPARE
        String inputString = "8\n".repeat(11);
        refreshInputStream(inputString);

        // GIVEN
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        String expectedErrorMessage = "Error during reading int value";

        assertThrows(IllegalArgumentException.class,
                () -> streamsIOService.readIntForRange(min, max, errorMessage),
                expectedErrorMessage);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRange_illegalArgumentException_enteredIncorrectString() {
        // PREPARE
        String correctInput = "2";
        String inputString = "8\n".repeat(5).concat(correctInput);
        refreshInputStream(inputString);

        // GIVEN
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(correctInput);

        // EXECUTE
        int result = streamsIOService.readIntForRange(min, max, errorMessage);

        // THAN
        assertThat(result).isEqualTo(expectedResult);
        verify(printStream, times(5)).println(errorMessage);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRangeWithPrompt_correctResultInt_enteredCorrectString() {
        // PREPARE
        String correctInput = "2";
        refreshInputStream(correctInput);

        // GIVEN
        int min = 1;
        int max = 4;
        String prompt = "Some prompt";
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(correctInput);

        // EXECUTE
        int result = streamsIOService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);

        // THAN
        assertThat(result).isEqualTo(expectedResult);
        verify(printStream).println(prompt);
    }

    private void refreshInputStream(String inputString) {
        inputStream = new ByteArrayInputStream(inputString.getBytes());
        streamsIOService = new StreamsIOService(printStream, inputStream);
    }
}