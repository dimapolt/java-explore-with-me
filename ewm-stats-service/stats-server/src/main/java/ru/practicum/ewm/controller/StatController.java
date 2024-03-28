package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.model.StatRequest;
import ru.practicum.ewm.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public StatDtoIn addStat(@RequestBody StatDtoIn statDto) {
        log.info("Запрос на добавление статистики");
        return service.addStat(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoOut> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(defaultValue = "") List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики");
        return service.getStats(new StatRequest(start, end, uris, unique));
    }
}