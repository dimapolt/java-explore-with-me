package ru.practicum.ewm.compilation.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationStorage extends JpaRepository<Compilation, Long> {

    @Query("SELECT c\n" +
            "FROM Compilation c\n" +
            "WHERE c.pinned = :pinned")
    List<Compilation> findAllWithPinned(Boolean pinned, Pageable pageable);

    boolean existsByTitle(String title);
}
