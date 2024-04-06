package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.dto.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.util.EwmRequest;
import ru.practicum.ewm.util.EwmValidationService;

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

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto eventDto, Long userId) {
        Event event = toEntity(eventDto, userId);
        validation.formEvent(event);

        return toFullDto(storage.save(event));
    }

    @Override
    @Transactional
    public List<EventFullDto> getEvents(Long userId, EwmRequest request) {
        validation.checkUserExists(userId);
        List<Event> events = storage.findAllByInitiatorId(userId, request.getPageable());

        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto getEvent(Long userId, Long eventId) {
        validation.checkUserExists(userId);
        Event event = searchEvent(eventId, userId);

        return toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(UpdateEventUserRequest eventDto, Long userId, Long eventId) {
        Event event = searchEvent(userId, eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongDataException("Изменить можно только отмененные события или события в состоянии ожидания");
        }

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

        if (eventDto.getEventDate() != null) {
            LocalDateTime evenDate = eventDto.getEventDate();
            validation.checkDate(evenDate);
            event.setEventDate(evenDate);
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

        if (eventDto.getStateAction().equals(CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        } else {
            event.setState(EventState.PENDING);
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        return toFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = storage.findById(eventId).orElseThrow(()-> new NoDataFoundException("Событие не найдено"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NoDataFoundException("Событие не опубликовано");
        }
        return toFullDto(event);
    }

    private Event searchEvent(Long id, Long userId) {
        Event event = storage.findById(id).orElseThrow(()-> new NoDataFoundException("Событие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new IllegalArgumentException("Пользователь с id = " + userId +
                    " не добавлял событие с id = " + id);
        }

        return event;
    }

}
