package com.example.practiceJournal.service;

import com.example.practiceJournal.api.response.WeatherResponse;
import com.example.practiceJournal.appCache.AppCache;
import com.example.practiceJournal.constants.PlaceHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    private final RedisService redisService;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }else {
            String url = appCache.APP_CACHE.get(PlaceHolder.WEATHER_API).replace(PlaceHolder.API_KEY, apiKey).replace(PlaceHolder.CITY, city);
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body != null) {
                redisService.set("weather_of_" + city, body, Duration.ofMinutes(5));
            }
            return body;
        }
    }
}
