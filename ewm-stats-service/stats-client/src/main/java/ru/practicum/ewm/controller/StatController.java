package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatClient;
import ru.practicum.ewm.dto.StatDtoIn;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.dto.StatValidate;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {
    private final StatClient statClient;

    @PostMapping("/hit")
    @Validated({StatValidate.OnCreate.class})
    public ResponseEntity<Object> addStat(@RequestBody @Valid StatDtoIn statDto) {
        log.info("Запрос на добавление статистики");
        return statClient.addStat(statDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatDtoOut>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                     @RequestParam(defaultValue = "") List<String> uris,
                                                     @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Запрос на получение статистики");
        return statClient.getStats(start, end, uris, unique);
    }
}
