package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.ServiceEvent;
import ru.practicum.ewm.util.EwmRequest;
import ru.practicum.ewm.util.EwmValidate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class EventControllerPrivateApi {
    private final ServiceEvent service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({EwmValidate.OnCreate.class})
    public EventFullDto createEvent(@RequestBody @Valid NewEventDto eventDto,
                                    @PathVariable Long userId) {
        log.info("Запрос на создание события");
        return service.createEvent(eventDto, userId);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "0")
                                            @PositiveOrZero(message = "Отрицательное значение " +
                                                                      "параметра 'from'") int from,
                                        @RequestParam(defaultValue = "10")
                                            @Positive(message = "Значение параметра 'size' - " +
                                                                "0 или отрицательное") int size) {
        log.info("Запрос на получение списка событий");
        return service.getEvents(userId, new EwmRequest(from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Запрос на получение события с id = " + eventId);
        return service.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@RequestBody @Valid UpdateEventUserRequest eventDto,
                                    @PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("Запрос на обновление события");
        return service.updateEvent(eventDto, userId, eventId);
    }
}
