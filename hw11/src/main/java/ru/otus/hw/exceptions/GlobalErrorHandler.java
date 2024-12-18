package ru.otus.hw.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
                .exceptionName(ex.getClass().getSimpleName())
                .message("Not correct value of parameters")
                .time(LocalDateTime.now())
                .build();

        Map<String, Object> errors = getErrorMap(ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("info", errorDetailsDto, "errors", errors));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(final RuntimeException ex) {

        ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
                .exceptionName(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDetailsDto);
    }

    private Map<String, Object> getErrorMap(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new LinkedHashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    String field = error.getField();
                    if (errors.get(field) == null) {
                        errors.put(field, error.getDefaultMessage());
                    } else {
                        Object message = errors.get(field);
                        List<Object> listMessage = new ArrayList<>(List.of(message));
                        listMessage.add(error.getDefaultMessage());
                        errors.put(field, listMessage);
                    }
                });
        return errors;
    }
}