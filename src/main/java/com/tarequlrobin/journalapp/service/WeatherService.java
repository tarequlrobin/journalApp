package com.tarequlrobin.journalapp.service;

import com.tarequlrobin.journalapp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private static final String apiKey = "6b55d6bf3e30d6b85ea056abb30819dc";
    private static final String API = "http://api.weatherstack.com/current?access_key=apiKey&query=city";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city) {
        String finalString = API.replace("apiKey", apiKey).replace("city", city);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalString, HttpMethod.GET, null,  WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }
}
