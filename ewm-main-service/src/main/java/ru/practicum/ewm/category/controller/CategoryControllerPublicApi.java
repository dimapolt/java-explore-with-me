package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.util.EwmRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
@Validated
public class CategoryControllerPublicApi {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0")
                                               @PositiveOrZero(message = "Отрицательное значение" +
                                                                         " параметра 'from'") int from,
                                           @RequestParam(defaultValue = "10")
                                               @Positive(message = "Значение параметра 'size' " +
                                                                   "0 или отрицательное") int size) {
        log.info("Запрос на получение категорий с {} по {} элементов", from, size);
        return service.getCategories(new EwmRequest(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable long catId) {
        log.info("Запрос на получение категории по id = " + catId);
        return service.getCategory(catId);
    }
}
