package ru.practicum.ewm.rating.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.rating.dto.EventRatingOut;
import ru.practicum.ewm.rating.dto.UserRatingOut;
import ru.practicum.ewm.rating.service.RatingService;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/ratings")
@Validated
public class RatingControllerPublicApi {
    private final RatingService service;

    @GetMapping("/events")
    public List<EventRatingOut> getEventsWithRating(@RequestParam(defaultValue = "0")
                                                    @PositiveOrZero(message = "Отрицательное " +
                                                            "значение параметра 'from'") int from,
                                                    @RequestParam(defaultValue = "10")
                                                    @Positive(message = "Значение параметра 'size' - " +
                                                            "0 или отрицательное") int size) {
        log.info("Получен запрос на получение событий с рейтингом");
        return service.getEventsWithRating(new EwmRequestParams(from, size, "rating"));
    }

    @GetMapping("/users")
    public List<UserRatingOut> getUsersWithRating(@RequestParam(defaultValue = "0")
                                                  @PositiveOrZero(message = "Отрицательное " +
                                                          "значение параметра 'from'") int from,
                                                  @RequestParam(defaultValue = "10")
                                                  @Positive(message = "Значение параметра 'size' - " +
                                                          "0 или отрицательное") int size) {
        log.info("Получен запрос на получение пользователей с рейтингом");
        return service.getUsersWithRating(new EwmRequestParams(from, size, "rating"));
    }
}
