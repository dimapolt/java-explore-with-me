package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.util.requests.EwmRequest;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, long catId);

    String deleteCategory(long catId);

    List<CategoryDto> getCategories(EwmRequest request);

    CategoryDto getCategory(long catId);
}
