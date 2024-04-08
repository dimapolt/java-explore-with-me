package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.dto.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.status.EvenStateAdmin;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.status.ReqStatus;
import ru.practicum.ewm.request.storage.RequestStorage;
import ru.practicum.ewm.util.EwmValidationService;
import ru.practicum.ewm.util.requests.EventsAdminRequest;
import ru.practicum.ewm.util.requests.EventsPublicRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.dto.mapper.EventMapper.toEntity;
import static ru.practicum.ewm.event.dto.mapper.EventMapper.toFullDto;
import static ru.practicum.ewm.event.model.status.EvenStateUser.CANCEL_REVIEW;

@Service
@RequiredArgsConstructor
public class ServiceEventImpl implements ServiceEvent {
    private final EventStorage storage;
    private final EwmValidationService validation;
    private final StatClient statClient;
    private final RequestStorage requestStorage;

    @Override
    @Transactional
    public EventFullDto createEventPrivate(NewEventDto eventDto, Long userId) {
        Event event = toEntity(eventDto, userId);
        validation.formEvent(event);
        event.setConfirmedRequests(requestStorage.getConfirmedRequests(event.getId()).orElse(0));

        return toFullDto(storage.save(event));
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsPrivate(Long userId, EwmRequestParams request) {
        validation.checkUserExists(userId);
        List<Event> events = storage.findAllByInitiatorId(userId, request.getPageable());
        events.forEach(event ->
                event.setConfirmedRequests(requestStorage.getConfirmedRequests(event.getId()).orElse(0)));

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEventPrivate(Long userId, Long eventId) {
        validation.checkUserExists(userId);
        Event event = searchEvent(userId, eventId);
        event.setConfirmedRequests(requestStorage.getConfirmedRequests(eventId).orElse(0));

        return toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventPrivate(UpdateEventUserRequest eventDto, Long userId, Long eventId) {
        Event event = searchEvent(eventId, userId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongDataException("Изменить можно только отмененные события или события в состоянии ожидания");
        }

        if (eventDto.getEventDate() != null) {
            LocalDateTime evenDate = eventDto.getEventDate();
            validation.checkDate(evenDate, 2);
            event.setEventDate(evenDate);
        }

        if (eventDto.getStateAction().equals(CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        } else {
            event.setState(EventState.PENDING);
        }

        updateEventFields(eventDto, event);
        event.setConfirmedRequests(requestStorage.getConfirmedRequests(eventId).orElse(0));

        return toFullDto(event);
    }

    @Override
    public List<RequestDto> getRequestsEventPrivate(Long userId, Long eventId) {
        searchEvent(eventId, userId);

        List<Request> requests = requestStorage.findAllByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatusPrivate(Long userId, Long eventId,
                                                                     EventRequestStatusUpdateRequest requestDto) {
        Event event = searchEvent(eventId, userId);

        event.setConfirmedRequests(requestStorage.getConfirmedRequests(eventId).orElse(0));

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new WrongDataException("Превышен лимит заявок");
        }

        EventRequestStatusUpdateResult resultUpdate = new EventRequestStatusUpdateResult();

        List<RequestDto> requests = requestStorage.findAllByIdIn(requestDto.getRequestIds()).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());

        if (requestDto.getStatus().equals(ReqStatus.CONFIRMED)) {
            requests.forEach(request -> request.setStatus(ReqStatus.CONFIRMED));
            resultUpdate.setConfirmedRequests(requests);
        } else if (requestDto.getStatus().equals(ReqStatus.REJECTED)) {
            requests.forEach(request -> request.setStatus(ReqStatus.REJECTED));
            resultUpdate.setRejectedRequests(requests);
        }

        return resultUpdate;
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsAdmin(EventsAdminRequest params) {
        LocalDateTime start = params.getRangeStart();
        LocalDateTime end = params.getRangeEnd();

        if (start == null) {
            start = LocalDateTime.now().minusYears(10);
        }

        if (end == null) {
            end = LocalDateTime.now().plusYears(10);
        }

        validation.checkStartEnd(start, end);

        List<Event> events = storage.findEventsForAdmin(params.getUsers(),
                params.getStates(),
                params.getCategories(),
                start,
                end,
                params.getPage().getPageable());

        events.forEach(event ->
                event.setConfirmedRequests(requestStorage.getConfirmedRequests(event.getId()).orElse(0)));

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest eventDto) {
        Event event = validation.checkEventExists(eventId);
        validation.checkDate(event.getEventDate(), 1);

        if (eventDto.getEventDate() != null) {
            LocalDateTime evenDate = eventDto.getEventDate();
            validation.checkDate(evenDate, 1);
            event.setEventDate(evenDate);
        }

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(EvenStateAdmin.PUBLISH_EVENT)) {
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new WrongDataException("Событие можно публиковать, только " +
                            "если оно в состоянии ожидания публикации");
                }
            } else if (eventDto.getStateAction().equals(EvenStateAdmin.REJECT_EVENT)) {
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    event.setState(EventState.CANCELED);
                } else {
                    throw new WrongDataException("Событие можно отклонить, только если оно еще не опубликовано");
                }
            }
        }

        updateEventFields(eventDto, event);
        event.setConfirmedRequests(requestStorage.getConfirmedRequests(eventId).orElse(0));
        return toFullDto(storage.save(event));
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsPublic(EventsPublicRequest params, HttpServletRequest request) {
        sendStat(request);

        LocalDateTime start = params.getRangeStart();
        LocalDateTime end = params.getRangeEnd();

        if (start == null) {
            start = LocalDateTime.now().minusYears(10);
        }
        if (end == null) {
            end = LocalDateTime.now().plusYears(10);
        }

        List<Event> events = storage.findEventsForPublic(params.getText(), params.getCategories(), params.getPaid(),
                start, end,
                params.getOnlyAvailable(), params.getPage().getPageable());


        events.forEach(event -> event.setConfirmedRequests(
                requestStorage.getConfirmedRequests(event.getId()).orElse(0)));

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEventPublic(Long id, HttpServletRequest request) {
        sendStat(request);

        Event event = storage.findById(id).orElseThrow(() -> new NoDataFoundException("Событие не найдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NoDataFoundException("Событие не опубликовано");
        }

        event.setConfirmedRequests(requestStorage.getConfirmedRequests(id).orElse(0));
        updateViews(event, List.of(request.getRequestURI()));

        return toFullDto(event);
    }

    private Event searchEvent(Long eventId, Long userId) {
        Event event = storage.findById(eventId).orElseThrow(() -> new NoDataFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь с id = " + userId +
                    " не добавлял событие с id = " + eventId);
        }

        return event;
    }

    private void updateEventFields(UpdateEventDto eventDto, Event event) {
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            Category category = validation.checkCategoryExists(event.getCategory().getId());
            event.setCategory(category);
        }

        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getLocation() != null) {
            validation.checkAndSetLocation(eventDto.getLocation(), event);
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
    }

    private void updateViews(Event event, List<String> uris) {
        List<StatDtoOut> stats = statClient.getStats(LocalDateTime.now().minusYears(10),
                LocalDateTime.now().plusYears(10), uris, true).getBody();

        if (stats != null) {
            if (stats.size() > 0) {
                event.setViews(stats.get(0).getHits());
            }
        }

        storage.save(event);
    }

    private void sendStat(HttpServletRequest request) {
        StatDtoIn statDtoIn = new StatDtoIn("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        statClient.addStat(statDtoIn);
    }

}
