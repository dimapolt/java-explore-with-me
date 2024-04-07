package ru.practicum.ewm.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestStorage extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    @Query("SELECT COUNT(r.requester)\n" +
          "FROM Request r\n" +
          "WHERE r.event = ?1 AND r.status = 'CONFIRMED'")
    Integer getConfirmedRequests(Long eventId);

}
