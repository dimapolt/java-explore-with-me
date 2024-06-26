package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.util.EwmValidate;
import ru.practicum.ewm.util.requests.EwmRequestParams;

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
    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({EwmValidate.OnCreate.class})
    public EventFullDto createEventPrivate(@RequestBody @Valid NewEventDto eventDto,
                                           @PathVariable Long userId) {
        log.info("Запрос на создание события");
        return service.createEventPrivate(eventDto, userId);
    }

    @GetMapping
    public List<EventFullDto> getEventsPrivate(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0")
                                               @PositiveOrZero(message = "Отрицательное значение " +
                                                       "параметра 'from'") int from,
                                               @RequestParam(defaultValue = "10")
                                               @Positive(message = "Значение параметра 'size' - " +
                                                       "0 или отрицательное") int size) {
        log.info("Запрос на получение списка событий");
        return service.getEventsPrivate(userId, new EwmRequestParams(from, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventPrivate(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("Запрос на получение события с id = " + eventId);
        return service.getEventPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @Validated({EwmValidate.OnUpdate.class})
    public EventFullDto updateEventPrivate(@RequestBody @Valid UpdateEventUserRequest eventDto,
                                           @PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.info("Запрос на обновление события");
        return service.updateEventPrivate(eventDto, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsEventPrivate(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("Запрос на получение всех запросов на участие в событии");
        return service.getRequestsEventPrivate(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatusPrivate(@PathVariable Long userId,
                                                                     @PathVariable Long eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest requestDto) {
        log.info("Запрос на изменение статуса запроса на участие в событии");
        return service.changeRequestStatusPrivate(userId, eventId, requestDto);
    }

}