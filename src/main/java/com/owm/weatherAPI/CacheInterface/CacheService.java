package com.owm.weatherAPI.CacheInterface;

import com.owm.weatherAPI.DTO.WeatherInformation;
import org.springframework.stereotype.Component;

@Component
public interface CacheService {
    WeatherInformation getWeatherFromCache(String key);

    void cacheWeather(String key, WeatherInformation weatherInformation);
}

