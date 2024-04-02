package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Класс содержащий сообщение о причинах исключения для ответа "фронтенду", согласно
 * <a href="https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json/">
 * спецификации
 * </a>
 */
@RequiredArgsConstructor
@Getter
public class ErrorDescription {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
