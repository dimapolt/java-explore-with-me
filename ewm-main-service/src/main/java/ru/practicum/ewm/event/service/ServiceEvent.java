package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.util.requests.EventsAdminRequest;
import ru.practicum.ewm.util.requests.EwmRequest;

import java.util.List;

public interface ServiceEvent {

    EventFullDto createEventPrivate(NewEventDto eventDto, Long userId);

    List<EventFullDto> getEventsPrivate(Long userId, EwmRequest request);

    EventFullDto getEventPrivate(Long userId, Long eventId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest eventDto, Long userId, Long eventId);

    List<EventFullDto> getEventsAdmin(EventsAdminRequest params);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest eventDto);
}
