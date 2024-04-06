package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.util.requests.EwmRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.dto.CategoryMapper.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryStorage storage;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = toEntity(categoryDto);
        return toDto(storage.save(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, long catId) {
        Category category = findCategory(catId);
        category.setName(categoryDto.getName());

        return toDto(category);
    }

    @Override
    @Transactional
    public String deleteCategory(long catId) {
        // Сделать проверку на события

        storage.delete(findCategory(catId));
        return "Категория удалена";
    }

    @Override
    @Transactional
    public List<CategoryDto> getCategories(EwmRequest request) {
        List<Category> categories = storage.findAll(request.getPageable()).getContent();
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto getCategory(long catId) {
        return toDto(findCategory(catId));
    }

    private Category findCategory(long catId) {
        return storage.findById(catId).orElseThrow(
                () -> new NoDataFoundException("Категория с id = " + catId + "не найдена!"));
    }
}
