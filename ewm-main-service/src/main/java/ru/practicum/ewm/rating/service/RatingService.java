package ru.practicum.ewm.rating.service;

import ru.practicum.ewm.rating.dto.EventRatingOut;
import ru.practicum.ewm.rating.dto.UserRatingOut;
import ru.practicum.ewm.rating.model.Rating;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.util.List;

public interface RatingService {
    Rating addMark(Long userId, Long eventId, Boolean mark);

    Rating changeMark(Long userId, Long eventId);

    String deleteMark(Long userId, Long eventId);

    List<EventRatingOut> getEventsWithRating(EwmRequestParams params);

    List<UserRatingOut> getUsersWithRating(EwmRequestParams params);
}
