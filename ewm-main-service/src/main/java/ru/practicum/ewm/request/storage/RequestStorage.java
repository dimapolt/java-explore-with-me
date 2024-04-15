package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.dto.RequestCount;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestStorage extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByIdIn(List<Long> eventIds);

    @Query("SELECT COUNT(r.id)\n" +
            "FROM Request r\n" +
            "WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Optional<Long> getConfirmedRequests(Long eventId);

    @Query("SELECT new ru.practicum.ewm.event.dto.RequestCount(r.event.id, COUNT(r.id))\n" +
            "FROM Request r\n" +
            "WHERE r.event.id IN :eventIds AND r.status = 'CONFIRMED'\n" +
            "GROUP BY r.event.id")
    List<RequestCount> getConfirmedRequests(List<Long> eventIds);
}
