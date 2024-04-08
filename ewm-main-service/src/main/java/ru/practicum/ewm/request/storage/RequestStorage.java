package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestStorage extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByIdIn(List<Long> eventIds);

    @Query("SELECT COUNT(r.requester)\n" +
            "FROM Request r\n" +
            "WHERE r.event.id = ?1 AND r.status = 'CONFIRMED'")
    Optional<Integer> getConfirmedRequests(Long eventId);

}
