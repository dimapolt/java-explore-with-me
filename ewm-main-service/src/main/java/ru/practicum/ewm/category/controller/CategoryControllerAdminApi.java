package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/categories")
@Validated
public class CategoryControllerAdminApi {
    private final CategoryService service;

    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на создание категории");
        return service.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable long catId) {
        log.info("Запрос на обновление категории");
        return service.updateCategory(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    public String deleteCategory(@PathVariable long catId) {
        log.info("Запрос на удаление категории");
        return service.deleteCategory(catId);
    }
}
