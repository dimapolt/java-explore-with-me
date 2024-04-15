package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchNoDataFoundException(NoDataFoundException e) {
        log.warn(e.getMessage());
        ErrorDescription description = new ErrorDescription(HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(description, HttpStatus.NOT_FOUND);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        List<ErrorDescription> response = new ArrayList<>();

        e.getBindingResult()
                .getAllErrors()
                .forEach((error) -> response.add(
                        new ErrorDescription(HttpStatus.BAD_REQUEST,
                                error.getClass().getSimpleName(),
                                error.getDefaultMessage(),
                                LocalDateTime.now()))
                );


        log.warn("Ошибка, связанная с невалидными полями");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> onConstraintValidationException(
            ConstraintViolationException e) {
        log.warn(e.getMessage());
        List<ErrorDescription> response = e.getConstraintViolations().stream()
                .map(error -> new ErrorDescription(HttpStatus.BAD_REQUEST,
                        e.getClass().getSimpleName(),
                        error.getMessage(),
                        LocalDateTime.now()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchAlreadyExistException(AlreadyExistException e) {
        log.warn(e.getMessage());
        ErrorDescription description = new ErrorDescription(HttpStatus.CONFLICT,
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(description, description.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchWrongDataException(WrongDataException e) {
        log.warn(e.getMessage());
        ErrorDescription description = new ErrorDescription(HttpStatus.CONFLICT,
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(description, description.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchIllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        ErrorDescription error = new ErrorDescription(HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchSQLException(SQLException e) {
        log.warn(e.getMessage());
        ErrorDescription description = new ErrorDescription(HttpStatus.CONFLICT,
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now());

        return new ResponseEntity<>(description, description.getStatus());
    }
}
