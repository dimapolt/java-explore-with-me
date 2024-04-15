package ru.practicum.ewm.rating.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.rating.service.RatingService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/ratings/users/{userId}/events/{eventId}")
@Validated
public class RatingControllerPrivateApi {
    private final RatingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating addMark(@PathVariable Long userId,
                          @PathVariable Long eventId,
                          @RequestParam(required = false) @NotNull(message = "Отсутствует оценка") Boolean mark) {
        log.info("Запрос на установление оценки {} от пользователя с id = {} событию с id = {}", mark, userId, eventId);
        return service.addMark(userId, eventId, mark);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public Rating changeMark(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        log.info("Запрос на изменение оценки от пользователя с id = {} событию с id = {}", userId, eventId);
        return service.changeMark(userId, eventId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteMark(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        log.info("Запрос на удаление оценки от пользователя с id = {} событию с id = {}", userId, eventId);
        return service.deleteMark(userId, eventId);
    }

}
