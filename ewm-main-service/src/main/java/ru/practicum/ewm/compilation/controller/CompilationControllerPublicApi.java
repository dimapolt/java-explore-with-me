package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/compilations")
@Validated
public class CompilationControllerPublicApi {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0")
                                                @PositiveOrZero(message = "Отрицательное " +
                                                        "значение параметра 'from'") int from,
                                                @RequestParam(defaultValue = "10")
                                                @Positive(message = "Значение параметра 'size' - " +
                                                        "0 или отрицательное") int size) {
        log.info("Публичный запрос на получение подборок по параметрам");
        return service.getCompilations(pinned, new EwmRequestParams(from, size));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Публичный запрос на получение подборки с id = " + compId);
        return service.getCompilation(compId);
    }
}
