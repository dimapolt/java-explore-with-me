package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.util.requests.EventsAdminRequest;
import ru.practicum.ewm.util.requests.EventsPublicRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto createEventPrivate(NewEventDto eventDto, Long userId);

    List<EventFullDto> getEventsPrivate(Long userId, EwmRequestParams request);

    EventFullDto getEventPrivate(Long userId, Long eventId);

    EventFullDto updateEventPrivate(UpdateEventUserRequest eventDto, Long userId, Long eventId);

    List<RequestDto> getRequestsEventPrivate(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatusPrivate(Long userId, Long eventId, EventRequestStatusUpdateRequest requestDto);

    List<EventFullDto> getEventsAdmin(EventsAdminRequest params);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest eventDto);

    List<EventFullDto> getEventsPublic(EventsPublicRequest eventsPublicRequest, HttpServletRequest request);

    EventFullDto getEventPublic(Long id, HttpServletRequest httpServletRequest);
}
