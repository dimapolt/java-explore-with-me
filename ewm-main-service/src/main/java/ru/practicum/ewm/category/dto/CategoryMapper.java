package ru.practicum.ewm.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public static Category toEntity(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(),
                            categoryDto.getName());
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(),
                               category.getName());
    }

}
