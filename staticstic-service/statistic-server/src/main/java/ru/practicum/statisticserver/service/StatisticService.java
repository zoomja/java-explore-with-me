package ru.practicum.statisticserver.service;

import dto.RequestDto;
import dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.statisticserver.mapper.StatisticMapper;
import ru.practicum.statisticserver.model.Statistic;
import ru.practicum.statisticserver.repository.StatisticRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final StatisticMapper statisticMapper;
    private final StatisticRepository statisticRepository;

    public void saveStatistic(RequestDto requestDto) {
        Statistic statistic = statisticMapper.toStatistic(requestDto);
        statisticRepository.save(statistic);
        log.info("statistic сохранён: {}", statistic);
    }

    public List<ResponseDto> getStatistics(LocalDateTime startTime, LocalDateTime endTime, List<String> uris, boolean unique) {
        if (endTime.isBefore(startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата окончания не может быть до даты начала");
        }
        if (uris.isEmpty()) {
            return statisticRepository.findStatisticByTimeRange(startTime, endTime);
        }
        if (unique) return statisticRepository.findStatisticByTimeAndUriUnique(startTime, endTime, uris);

        return statisticRepository.findStatisticByTimeAndUriNotUnique(startTime, endTime, uris);
    }
}
