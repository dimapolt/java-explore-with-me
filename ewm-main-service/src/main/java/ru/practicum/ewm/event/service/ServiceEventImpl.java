package ru.practicum.ewm.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.ewm.request.storage.RequestStorage;
import ru.practicum.ewm.util.requests.EventsAdminRequest;
import ru.practicum.ewm.util.requests.EventsPublicRequest;
import ru.practicum.ewm.util.requests.EwmRequestParams;
import ru.practicum.ewm.util.EwmValidationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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

        return toFullDto(storage.save(event));
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsPrivate(Long userId, EwmRequestParams request) {
        validation.checkUserExists(userId);
        List<Event> events = storage.findAllByInitiatorId(userId, request.getPageable());

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEventPrivate(Long userId, Long eventId) {
        validation.checkUserExists(userId);
        Event event = searchEvent(eventId, userId);

        return toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEventPrivate(UpdateEventUserRequest eventDto, Long userId, Long eventId) {
        Event event = searchEvent(userId, eventId);

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
        return toFullDto(event);
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsAdmin(EventsAdminRequest params) {
        List<Event> events = storage.findEventsForAdmin(params.getUsers(),
                params.getStates(),
                params.getCategories(),
                params.getRangeStart(),
                params.getRangeEnd(),
                params.getPage().getPageable());

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
        return toFullDto(storage.save(event));
    }

    @Override
    @Transactional
    public List<EventFullDto> getEventsPublic(EventsPublicRequest eventsPublicRequest, HttpServletRequest request) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto getEventPublic(Long id, HttpServletRequest request) {
        Event event = storage.findById(id).orElseThrow(() -> new NoDataFoundException("Событие не найдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongDataException("Событие не опубликовано");
        }
        StatDtoIn statDtoIn = new StatDtoIn("ewm-main-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());

        event.setConfirmedRequests(requestStorage.getConfirmedRequests(id));
        updateViews(event, List.of(request.getRequestURI()));

        statClient.addStat(statDtoIn);

        return toFullDto(event);
    }

    private Event searchEvent(Long id, Long userId) {
        Event event = storage.findById(id).orElseThrow(() -> new NoDataFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь с id = " + userId +
                    " не добавлял событие с id = " + id);
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
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<Object> response =  statClient.getStats(LocalDateTime.MIN,
                LocalDateTime.MAX, uris, true);

        List<StatDtoOut> stats = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });

       event.setViews(stats.get(0).getHits());

    }


}
