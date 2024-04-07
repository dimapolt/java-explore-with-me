package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
@Validated
public class RequestControllerPrivateApi {
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam(required = false) Long eventId) {
        log.info("Запрос на участие в событии с id = " + eventId + ", от пользователя с id = " + userId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        log.info("Запрос на получение всех заявок для пользователя с id = " + userId);
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("Запрос на отмену заявки с id = " + requestId + ", от пользователя с id = " + userId);
        return requestService.cancelRequest(userId, requestId);
    }

}
