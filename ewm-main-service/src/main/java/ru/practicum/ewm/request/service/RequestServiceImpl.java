package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.status.EventState;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.status.ReqStatus;
import ru.practicum.ewm.request.storage.RequestStorage;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.util.EwmValidationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.dto.mapper.RequestMapper.toDto;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final EwmValidationService validation;
    private final RequestStorage storage;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = validation.checkUserExists(userId);
        Event event = validation.checkEventExists(eventId);

        Optional<Request> eventO = storage.findByRequesterIdAndEventId(userId, eventId);

        if (eventO.isPresent()) {
            throw new WrongDataException("Заявка уже подана");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new WrongDataException("Событие не опубликовано");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongDataException("Пользователь отправляет заявку на своё событие");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new WrongDataException("Достигнуто ограничение на число заявок у данного события");
        }

        ReqStatus status = event.isRequestModeration() ? ReqStatus.PENDING : ReqStatus.CONFIRMED;
        Request request = new Request(null, LocalDateTime.now(), event, requester, status);

        return toDto(storage.save(request));
    }

    @Override
    @Transactional
    public List<RequestDto> getRequests(Long userId) {
        validation.checkUserExists(userId);
        List<Request> requests = storage.findAllByRequesterId(userId);

        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        validation.checkUserExists(userId);
        Request request = storage.findById(requestId).orElseThrow(
                () -> new NoDataFoundException("Событие с id = " + requestId + " не найдено"));

        if (!request.getEvent().getInitiator().getId().equals(userId)) {
            throw new WrongDataException("Пользователь не может отклонить запрос не своего события");
        }

        request.setStatus(ReqStatus.REJECTED);
        return toDto(request);
    }
}
