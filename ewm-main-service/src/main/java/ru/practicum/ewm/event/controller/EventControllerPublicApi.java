package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.util.requests.EventsPublicRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/events")
@Validated
public class EventControllerPublicApi {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getEventsPublic(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                              @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                              @RequestParam(defaultValue = "0")
                                              @PositiveOrZero(message = "Отрицательное значение " +
                                                      "параметра 'from'") int from,
                                              @RequestParam(defaultValue = "10")
                                              @Positive(message = "Значение параметра 'size' - " +
                                                      "0 или отрицательное") int size,
                                              HttpServletRequest httpServletRequest) {
        log.info("Публичный запрос на поиск событий по параметрам");
        if (sort.equals("EVENT_DATE")) {
            sort = "eventDate";
        } else if (sort.equals("VIEWS")) {
            sort = "views";
        } else {
            throw new IllegalArgumentException("Неверный параметр сортировки! Правильные варианты: EVENT_DATE, VIEWS");
        }

        EwmRequestParams page = new EwmRequestParams(from, size, sort);
        EventsPublicRequest eventsPublicRequest = new EventsPublicRequest(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, page);

        return service.getEventsPublic(eventsPublicRequest, httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublic(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Публичный запрос на получение события по id = " + id);
        return service.getEventPublic(id, httpServletRequest);
    }

}
