package client;

import dto.RequestDto;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StatsClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public void saveStatistic(RequestDto requestDto) {
        HttpEntity<RequestDto> request = new HttpEntity<>(requestDto);
//        restTemplate.postForObject("http://localhost:9090/hit", request, String.class);
        restTemplate.postForObject("http://stats-server:9090/hit", request, String.class);
    }
}