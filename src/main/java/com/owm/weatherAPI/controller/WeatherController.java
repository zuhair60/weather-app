package com.owm.weatherAPI.controller;

import com.owm.weatherAPI.DTO.WeatherInformation;
import com.owm.weatherAPI.Exception.WeatherApiException;
import com.owm.weatherAPI.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class WeatherController {

    WeatherService weatherService;

    Logger log = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;

    }

    @GetMapping("/get-weather")
    public ResponseEntity<WeatherInformation> getWeather(@RequestParam String cityName) throws WeatherApiException {
        log.info("getting weather details of city {}", cityName);
        WeatherInformation weatherInformation = weatherService.getWeatherData(cityName);
        return ResponseEntity.ok(weatherInformation);

    }

}


