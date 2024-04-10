package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    String deleteCategory(Long catId);

    List<CategoryDto> getCategories(EwmRequestParams request);

    CategoryDto getCategory(Long catId);
}
