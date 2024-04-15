package ru.practicum.ewm.rating.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.rating.dto.EventRating;
import ru.practicum.ewm.rating.dto.UserRating;
import ru.practicum.ewm.rating.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingStorage extends JpaRepository<Rating, Long> {
    String WILSON = "((r3.pos + 1.9208) / (r3.pos + r3.neg) " +
            "- 1.96 * SQRT((r3.pos * r3.neg) / (r3.pos + r3.neg) + 0.9604) / " +
            "(r3.pos + r3.neg)) / (1 + 3.8416 / (r3.pos + r3.neg))";

    @Query(value = "SELECT r3.\"event\", " + WILSON + " rating FROM (\n" +
            "SELECT r1.\"event\", r1.rate, COALESCE(r1.rCount, 0) pos, r2.rate, COALESCE(r2.rCount, 0) neg\n" +
            "FROM (SELECT r.\"event\", r.rate, COUNT(r.rate) rCount  \n" +
            "      FROM ratings r \n" +
            "      WHERE r.rate = true \n" +
            "      GROUP BY r.rate, r.\"event\") r1 \n" +
            " LEFT JOIN (SELECT r.\"event\", r.rate, COUNT(r.rate) rCount  \n" +
            "            FROM ratings r \n" +
            "            WHERE r.rate = false \n" +
            "            GROUP BY r.rate, r.\"event\") r2 ON r1.\"event\" = r2.\"event\"  \n" +
            ") r3", nativeQuery = true)
    List<EventRating> getRatingEvents(Pageable pageable);

    @Query(value = "select r3.initiator, " + WILSON + " rating from (\n" +
            "select r1.initiator, r1.rate, coalesce(r1.summ, 0) pos, r2.rate, coalesce(r2.summ, 0) neg\n" +
            "from (select r.initiator, r.rate, count(r.rate) summ  \n" +
            "      from ratings r \n" +
            "      where r.rate = true \n" +
            "      group by r.rate, r.initiator) r1 \n" +
            " left join (select r.initiator, r.rate, count(r.rate) summ  \n" +
            "            from ratings r \n" +
            "            where r.rate = false \n" +
            "            group by r.rate, r.initiator) r2 on r1.initiator = r2.initiator\n" +
            ") r3", nativeQuery = true)
    List<UserRating> getRatingUsers(Pageable pageable);

    Optional<Rating> findByRaterIdAndEventId(Long raterId, Long eventId);
}
