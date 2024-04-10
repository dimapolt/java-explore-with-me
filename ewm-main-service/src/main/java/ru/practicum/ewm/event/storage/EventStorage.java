package ru.practicum.ewm.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.status.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventStorage extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    Optional<Event> findByCategoryId(Long catId);

    @Query("SELECT e\n" +
            "FROM Event e\n" +
            "WHERE 1=1\n" +
            "AND (:users IS NULL OR e.initiator.id IN :users)\n" +
            "AND (:states IS NULL OR e.state IN :states)\n" +
            "AND (:categories IS NULL OR e.category.id IN :categories)\n" +
            "AND (e.eventDate > :start)\n" +
            "AND (e.eventDate < :end)")
    List<Event> findEventsForAdmin(List<Long> users,
                                   List<EventState> states,
                                   List<Long> categories,
                                   LocalDateTime start,
                                   LocalDateTime end,
                                   Pageable pageable);

    @Query("SELECT e\n" +
            "FROM Event e\n" +
            "WHERE 1=1\n" +
            "AND e.state = 'PUBLISHED'\n" +
            "AND (:text IS NULL OR ((LOWER(e.annotation) LIKE CONCAT('%', lower(:text), '%'))\n" +
            "OR (LOWER(e.description) LIKE CONCAT('%', lower(:text), '%'))))\n" +
            "AND (:categories IS NULL OR e.category.id IN :categories)\n" +
            "AND (:paid IS NULL OR e.paid = :paid)\n" +
            "AND (e.eventDate > :start) AND (e.eventDate < :end)\n" +
            "AND (:available = false OR (:available = true AND (e.participantLimit > " +
            "(SELECT count(*)\n" +
            "FROM Request AS r\n" +
            "WHERE e.id = r.event.id\n" +
            "AND r.status = 'CONFIRMED')\n" +
            "OR (e.participantLimit = 0 )))) ")
    List<Event> findEventsForPublic(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    boolean available,
                                    Pageable pageable);

}