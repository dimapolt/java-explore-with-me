package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.util.EwmRequest;

import java.util.List;

public interface ServiceEvent {

    EventFullDto createEvent(NewEventDto eventDto, Long userId);

    List<EventFullDto> getEvents(Long userId, EwmRequest request);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(UpdateEventUserRequest eventDto, Long userId, Long eventId);

    EventFullDto getEvent(Long eventId);
}
