package ru.practicum.statisticserver.controller;

import dto.RequestDto;
import dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statisticserver.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @PostMapping("/hit")
    public void saveHit(@RequestBody RequestDto requestDto ) {
        statisticService.saveStatistic(requestDto);
    }

    @GetMapping("/stats")
    public List<ResponseDto> getStatistics(@RequestParam LocalDateTime start,
                                           @RequestParam LocalDateTime end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) Boolean unique) {
        return statisticService.getStatistics(start, end, uris, unique);
    }
}
