package ru.practicum.statisticserver.repository;

import dto.ResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statisticserver.model.Statistic;


import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("""
            SELECT new dto.ResponseDto(s.app, s.uri, COUNT(s.ip))
            FROM Statistic s
            WHERE s.timestamp BETWEEN :startTime AND :endTime
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
    """)
    List<ResponseDto> findStatisticByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("""
            SELECT new dto.ResponseDto(s.app, s.uri, COUNT(distinct s.ip))
            FROM Statistic s
            WHERE s.timestamp BETWEEN :startTime AND :endTime
            AND s.uri IN (:uri)
            GROUP BY s.app, s.uri
            ORDER BY COUNT(distinct s.ip) DESC
    """)
    List<ResponseDto> findStatisticByTimeAndUriUnique(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("uri") List<String> uri);

    @Query("""
            SELECT new dto.ResponseDto(s.app, s.uri, COUNT(s.ip))
            FROM Statistic s
            WHERE s.timestamp BETWEEN :startTime AND :endTime
            AND s.uri IN (:uri)
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
    """)
    List<ResponseDto> findStatisticByTimeAndUriNotUnique(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("uri") List<String> uri);
}
