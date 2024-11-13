package ru.practicum.statisticserver.mapper;

import dto.RequestDto;
import org.mapstruct.Mapper;
import ru.practicum.statisticserver.model.Statistic;

@Mapper(componentModel = "spring")
public interface StatisticMapper {
    Statistic toStatistic(RequestDto requestDto);
}