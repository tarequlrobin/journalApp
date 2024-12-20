package com.tarequlrobin.journalapp.service;

import com.tarequlrobin.journalapp.api.response.WeatherResponse;
import com.tarequlrobin.journalapp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
    //private static final String API = "http://api.weatherstack.com/current?access_key=apiKey&query=city";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("Weather_of_" + city, WeatherResponse.class);
        if (weatherResponse != null) {
            return weatherResponse;
        }
        else {
            String finalString = appCache.APP_CACHE.get("weather_api").replace("<apiKey>", apiKey).replace("<city>", city);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalString, HttpMethod.GET, null,  WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body != null) {
                redisService.set("Weather_of_" + city, body, 300l);
            }
            return body;
        }

    }
}
