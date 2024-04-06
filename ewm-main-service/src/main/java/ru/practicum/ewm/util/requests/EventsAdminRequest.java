package ru.practicum.ewm.util.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.model.status.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventsAdminRequest {
    private final List<Long> users;
    private final List<EventState> states;
    private final List<Long> categories;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final EwmRequest params;
}
