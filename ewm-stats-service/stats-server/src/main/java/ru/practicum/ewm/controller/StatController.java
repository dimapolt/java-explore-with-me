package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.dto.StatValidate;
import ru.practicum.ewm.service.StatService;
import ru.practicum.ewm.util.StatRequest;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @Validated({StatValidate.OnCreate.class})
    public StatDtoIn addStat(@RequestBody @Valid StatDtoIn statDto) {
        log.info("Запрос на добавление статистики");
        return service.addStat(statDto);
    }

    @GetMapping("/stats")
    public List<StatDtoOut> getStats(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(defaultValue = "") List<String> uris,
                                     @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики");
        if (start == null) {
            start = LocalDateTime.now().minusYears(10);
        }
        if (end == null) {
            end = LocalDateTime.now().plusYears(10);
        }

        return service.getStats(new StatRequest(start, end, uris, unique));
    }
}
