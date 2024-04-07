package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.event.service.ServiceEvent;
import ru.practicum.ewm.util.requests.EventsAdminRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/events")
@Validated
public class EventControllerAdminApi {
    private final ServiceEvent service;
    @GetMapping
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0")
                                        @PositiveOrZero(message = "Отрицательное значение " +
                                                "параметра 'from'") int from,
                                        @RequestParam(defaultValue = "10")
                                        @Positive(message = "Значение параметра 'size' - " +
                                                "0 или отрицательное") int size) {
        log.info("Запрос на получение списка событий администратором");
        EwmRequestParams ewmRequestParams = new EwmRequestParams(from, size);
        EventsAdminRequest eventsAdminRequest = new EventsAdminRequest(users, states, categories,
                                                                       rangeStart, rangeEnd, ewmRequestParams);
        return service.getEventsAdmin(eventsAdminRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest eventAdminDto) {
        log.info("Запрос на обновление события администратором");
        return service.updateEventAdmin(eventId, eventAdminDto);
    }
}
