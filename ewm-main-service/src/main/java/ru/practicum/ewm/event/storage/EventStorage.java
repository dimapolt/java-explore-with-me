package ru.practicum.ewm.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.status.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventStorage extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long id, Pageable pageable);

    @Query("SELECT e\n" +
           "FROM Event e\n" +
           "WHERE 1=1\n" +
                 "AND (e.initiator.id IN ?1 OR ?1 IS NULL) " +
                 "AND (e.state IN ?2 OR ?2 IS NULL) " +
                 "AND (e.category.id IN ?3 OR ?3 IS NULL) " +
                 "AND (e.eventDate > ?4 OR ?4 IS NULL) " +
                 "AND (e.eventDate < ?5 OR ?5 IS NULL)")
    List<Event> findEventsForAdmin(List<Long> users,
                                   List<EventState> states,
                                   List<Long> categories,
                                   LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd,
                                   Pageable pageable);
    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE ((?1 IS null) OR ((lower(e.annotation) LIKE concat('%', lower(?1), '%')) " +
                "OR (lower(e.description) LIKE concat('%', lower(?1), '%')))) " +
            "AND (e.category.id IN ?2 OR ?2 IS null) " +
            "AND (e.paid = ?3 OR ?3 IS null) " +
            "AND (e.eventDate > ?4 OR ?4 IS null) AND (e.eventDate < ?5 OR ?5 IS null) " +
            "AND (?6 = false OR ((?6 = true AND e.participantLimit > (SELECT count(*) FROM Request AS r WHERE e.id = r.event.id))) " +
            "OR (e.participantLimit > 0 )) ")
    List<Event> findEventsForPublic(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    boolean onlyAvailable,
                                    Pageable pageable);

}