package ru.practicum.ewm.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

@Data
@AllArgsConstructor
public class EventRatingOut {
    private EventShortDto event;
    private Float rating;
}
