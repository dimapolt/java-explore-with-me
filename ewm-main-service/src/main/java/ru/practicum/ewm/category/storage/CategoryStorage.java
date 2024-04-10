package ru.practicum.ewm.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.model.Category;

import java.util.Optional;

public interface CategoryStorage extends JpaRepository<Category, Long> {
    //boolean existByName(String name);

    Optional<Category> findByName(String name);
}
