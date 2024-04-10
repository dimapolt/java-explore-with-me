package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.storage.CategoryStorage;
import ru.practicum.ewm.event.storage.EventStorage;
import ru.practicum.ewm.exception.NoDataFoundException;
import ru.practicum.ewm.exception.WrongDataException;
import ru.practicum.ewm.util.requests.EwmRequestParams;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.dto.CategoryMapper.toDto;
import static ru.practicum.ewm.category.dto.CategoryMapper.toEntity;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryStorage storage;
    private final EventStorage eventStorage;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = toEntity(categoryDto);

        Optional<Category> categoryO = storage.findByName(categoryDto.getName());
        if (categoryO.isPresent()) {
            throw new WrongDataException("Данное название категории уже занято");
        }


        return toDto(storage.save(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        Category category = findCategory(catId);


        // Проверка, что в базе нет категории с именем как у обновления и что не обновляем имя на такое же
        if (!category.getName().equals(categoryDto.getName()) && storage.findByName(categoryDto.getName()).isPresent()) {
            throw new WrongDataException("Данное название категории уже занято");
        }

        category.setName(categoryDto.getName());
        return toDto(category);
    }

    @Override
    @Transactional
    public String deleteCategory(Long catId) {
        Category category = findCategory(catId);

        if (eventStorage.findByCategoryId(catId).isPresent()) {
            throw new WrongDataException("Нельзя удалить категорию, так как есть привязанное событие");
        }

        storage.delete(category);
        return "Категория удалена";
    }

    @Override
    @Transactional
    public List<CategoryDto> getCategories(EwmRequestParams request) {
        List<Category> categories = storage.findAll(request.getPageable()).getContent();
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto getCategory(Long catId) {
        return toDto(findCategory(catId));
    }

    private Category findCategory(Long catId) {
        return storage.findById(catId).orElseThrow(
                () -> new NoDataFoundException("Категория с id = " + catId + "не найдена!"));
    }
}
