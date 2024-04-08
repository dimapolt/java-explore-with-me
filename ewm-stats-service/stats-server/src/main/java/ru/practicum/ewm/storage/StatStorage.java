package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.StatDtoOut;
import ru.practicum.ewm.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatStorage extends JpaRepository<Stat, Long> {

    @Query("SELECT new ru.practicum.ewm.dto.StatDtoOut(st.app, st.uri, count(st.uri)) " +
            "FROM Stat as st " +
            "WHERE st.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY count(st.uri) DESC")
    List<StatDtoOut> findAllAndNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatDtoOut(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Stat as st " +
            "WHERE st.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY count(st.ip) DESC")
    List<StatDtoOut> findAllAndUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatDtoOut(st.app, st.uri, COUNT(st.ip)) " +
            "FROM Stat as st " +
            "WHERE st.timestamp BETWEEN ?1 AND ?2 " +
            "AND st.uri IN ?3 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY count(st.ip) DESC")
    List<StatDtoOut> findByUrisAndNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.dto.StatDtoOut(st.app, st.uri, COUNT(DISTINCT st.ip)) " +
            "FROM Stat as st " +
            "WHERE (st.timestamp BETWEEN ?1 AND ?2) " +
            "AND st.uri IN ?3 " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY count(st.ip) DESC")
    List<StatDtoOut> findByUrisAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
