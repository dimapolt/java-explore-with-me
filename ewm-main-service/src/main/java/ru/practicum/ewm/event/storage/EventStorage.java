package ru.practicum.ewm.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventStorage extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long id, Pageable pageable);
}