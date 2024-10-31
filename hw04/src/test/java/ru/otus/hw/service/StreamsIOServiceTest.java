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
class StreamsIOServiceTest {
    @InjectMocks
    private StreamsIOService streamsIOService;

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    @Test
    @DisplayName("should 'println' happened once when invoke method 'printLine'")
    void printLine() {
        // given
        String testString = "Test string";

        // when
        streamsIOService.printLine(testString);

        // then
        verify(printStream).println(testString);
        verifyNoMoreInteractions(printStream);
    }

    @Test
    @DisplayName("should 'printf' happened once when invoke method 'printFormattedLine'")
    void printFormattedLine() {
        // given
        String testString = "Test string";
        String resultString = testString.concat("%n");

        // when
        streamsIOService.printFormattedLine(testString);

        // then
        verify(printStream).printf(resultString);
        verifyNoMoreInteractions(printStream);
    }

    @Test
    @DisplayName("should return string what entered when invoke method 'readStringWithPrompt'")
    void readStringWithPrompt() {
        // given
        String prompt = "Some prompt";
        String inputString = "Some input string";

        refreshInputStream(inputString);

        // when
        String result = streamsIOService.readStringWithPrompt(prompt);

        // then
        assertThat(inputString).isEqualTo(result);
    }

    @Test
    @DisplayName("should return string what entered when invoke method 'readString'")
    void readString() {
        // given
        String inputString = "Some input string";

        refreshInputStream(inputString);

        // when
        String result = streamsIOService.readString();

        // then
        assertThat(inputString).isEqualTo(result);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRange_correctResultInt_enteredCorrectString() {
        // given
        String inputString = "2";
        refreshInputStream(inputString);
        
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(inputString);

        // when
        int result = streamsIOService.readIntForRange(min, max, errorMessage);

        // then
        assertThat(expectedResult).isEqualTo(result);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRange_illegalArgumentExceptionWithMessage_enteredIncorrectStringMaxAttempts() {
        String inputString = "8\n".repeat(11);
        refreshInputStream(inputString);

        // when
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        String expectedErrorMessage = "Error during reading int value";

        // then
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

        // given
        int min = 1;
        int max = 4;
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(correctInput);

        // when
        int result = streamsIOService.readIntForRange(min, max, errorMessage);

        // then
        assertThat(result).isEqualTo(expectedResult);
        verify(printStream, times(5)).println(errorMessage);
    }

    @Test
    @DisplayName("should return correct int what entered when invoke method 'readIntForRange'")
    void readIntForRangeWithPrompt_correctResultInt_enteredCorrectString() {
        // PREPARE
        String correctInput = "2";
        refreshInputStream(correctInput);

        // given
        int min = 1;
        int max = 4;
        String prompt = "Some prompt";
        String errorMessage = "Error message";
        int expectedResult = Integer.parseInt(correctInput);

        // when
        int result = streamsIOService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);

        // then
        assertThat(result).isEqualTo(expectedResult);
        verify(printStream).println(prompt);
    }

    private void refreshInputStream(String inputString) {
        inputStream = new ByteArrayInputStream(inputString.getBytes());
        streamsIOService = new StreamsIOService(printStream, inputStream);
    }
}