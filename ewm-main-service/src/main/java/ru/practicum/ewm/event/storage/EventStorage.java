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
            "AND (e.eventDate > :rangeStart)\n" +
            "AND (e.eventDate < :rangeEnd)")
    List<Event> findEventsForAdmin(List<Long> users,
                                   List<EventState> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);

    @Query("SELECT e\n" +
            "FROM Event e\n" +
            "WHERE ((?1 IS null) OR ((lower(e.annotation) LIKE concat('%', lower(?1), '%'))\n" +
            "OR (lower(e.description) LIKE concat('%', lower(?1), '%'))))\n" +
            "AND (e.category.id IN ?2 OR ?2 IS null)\n" +
            "AND (e.paid = ?3 OR ?3 IS null)\n" +
            "AND (e.eventDate > ?4) AND (e.eventDate < ?5)\n" +
            "AND (?6 = false OR ((?6 = true AND e.participantLimit > " +
            "(SELECT count(*)\n" +
            "FROM Request AS r WHERE e.id = r.event.id)))\n" +
            "OR (e.participantLimit > 0 )) ")
    List<Event> findEventsForPublic(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    boolean onlyAvailable,
                                    Pageable pageable);

}