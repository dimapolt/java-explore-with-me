package ru.practicum.ewm.util.requests;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventsPublicRequest {
    private final String text;
    private final List<Long> categories;
    private final Boolean paid;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Boolean onlyAvailable;
    private final EwmRequestParams page;
}
