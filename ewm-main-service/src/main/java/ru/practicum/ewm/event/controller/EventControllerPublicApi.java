package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.service.ServiceEvent;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
    private final ServiceEvent service;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam Boolean paid,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                        @RequestParam @NotNull String sort,
                                        @RequestParam(defaultValue = "0")
                                            @PositiveOrZero(message = "Отрицательное значение " +
                                                    "параметра 'from'") int from,
                                        @RequestParam(defaultValue = "10")
                                            @Positive(message = "Значение параметра 'size' - " +
                                                    "0 или отрицательное") int size) {
        log.info("Публичный запрос на поиск событий по параметрам");
        return null;
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.info("Публичный запрос на получение события по id = " + id);

        return null;
    }
}
