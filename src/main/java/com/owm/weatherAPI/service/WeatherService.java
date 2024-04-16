package com.owm.weatherAPI.service;

import com.owm.weatherAPI.CacheInterface.CacheService;
import com.owm.weatherAPI.DTO.WeatherInformation;
import com.owm.weatherAPI.Exception.WeatherApiException;
import com.owm.weatherAPI.config.ExternalServiceConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final ExternalServiceConfig config;
    private final RestTemplate restTemplate;
    private final CacheService cacheService;

    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);
//    private final String WEATHER_API_URL ="https://api.openweathermap.org/data/2.5/weather?q=";   // pused url using externalconfiguration class
//    private final String API_KEY ="98f1954840580779def0a284ec77ecc6";   / placed inside Constants class in the utility

    @Autowired
    public WeatherService(ExternalServiceConfig config, RestTemplate restTemplate, CacheService cacheService) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
    }

    public WeatherInformation getWeatherData(String cityName) throws WeatherApiException {
        String url = config.getOpenWeatherApiUrl() + "?q=" + cityName + "&appid=" + "98f1954840580779def0a284ec77ecc6";
       logger.info("Url constructed is : {}",url);
        String cacheKey = "weather_" + cityName;
        WeatherInformation cachedWeather = cacheService.getWeatherFromCache(cacheKey);
        if (cachedWeather != null) {
            logger.info("Cache Hit for city: {}", cityName);
            return cachedWeather;
        }

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
            logger.info("OWM API hit for city: {}", cityName);
        } catch (Exception e) {
            logger.error("Error fetching weather data from OWM API: {}", e.getMessage());
            throw new WeatherApiException("Error fetching weather data rom OWM API", e);
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                WeatherInformation weatherInformation = parseWeatherResponse(response.getBody());
                cacheService.cacheWeather(cacheKey, weatherInformation);
                return weatherInformation;
            } catch (JSONException e) {
                logger.error("Error parsing weather data from OWM API: {}", e.getMessage());
                throw new WeatherApiException("Error parsing weather data from OWM API", e);
            }
        }
        else {
            logger.error("Error fetching weather data from OWM API: Status code {}", response.getStatusCodeValue());
            throw new WeatherApiException("Error fetching weather data: Status code " + response.getStatusCodeValue());
        }
    }

    private WeatherInformation parseWeatherResponse(String responseBody) throws JSONException {
        JSONObject jsonObject = new JSONObject(responseBody);
        String main = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
        double temp = jsonObject.getJSONObject("main").getDouble("temp");
        double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
        return new WeatherInformation(main, temp, windSpeed);
    }
}
