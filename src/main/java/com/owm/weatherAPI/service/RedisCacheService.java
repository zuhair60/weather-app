package com.owm.weatherAPI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owm.weatherAPI.CacheInterface.CacheService;
import com.owm.weatherAPI.DTO.WeatherInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RedisCacheService implements CacheService {

    Logger log = LoggerFactory.getLogger(CacheService.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String WEATHER_CACHE = "weatherCache";  // Cache name


    @Override
    public WeatherInformation getWeatherFromCache(String key) {
        String cacheKey = WEATHER_CACHE + ":" + key;
        String serializedWeather = redisTemplate.opsForValue().get(cacheKey);
        if (serializedWeather != null) {
            log.info("Cache key: {}", cacheKey);
            ObjectMapper objectMapper = new ObjectMapper();  
            try {
                return objectMapper.readValue(serializedWeather, WeatherInformation.class);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing weather information from cache: {}", e.getMessage());
                return null;
            }
        }
        return null;
    }
    
    @Override
    public void cacheWeather(String key, WeatherInformation weatherInformation) {
        ObjectMapper objectMapper = new ObjectMapper(); 
        String serializedWeather = null;
        try {
            serializedWeather = objectMapper.writeValueAsString(weatherInformation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (serializedWeather != null) {
            String cacheKey = WEATHER_CACHE + ":" + key;
            redisTemplate.opsForValue().set(WEATHER_CACHE + ":" + key, serializedWeather);
            redisTemplate.expire(cacheKey, 60, TimeUnit.SECONDS); // Set expiration time (e.g., 60 seconds)
        }
        }
    }

