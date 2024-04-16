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
// A Java class implementing cache service using Redis for weather information storage and retrieval.

@Service
public class RedisCacheService implements CacheService {

    Logger log = LoggerFactory.getLogger(CacheService.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String WEATHER_CACHE = "weatherCache";  // Cache name

// A Java method to retrieve weather information from Redis cache using Jackson for deserialization.
    @Override
    public WeatherInformation getWeatherFromCache(String key) {
        String cacheKey = WEATHER_CACHE + ":" + key;
        String serializedWeather = redisTemplate.opsForValue().get(cacheKey);
        if (serializedWeather != null) {
            // Log the cache key for verification
            log.info("Cache key: {}", cacheKey);
            // Deserialize weather information (using Jackson if needed)
            ObjectMapper objectMapper = new ObjectMapper();  // Example using Jackson
            try {
                return objectMapper.readValue(serializedWeather, WeatherInformation.class);
            } catch (JsonProcessingException e) {
                // Handle deserialization exception
                log.error("Error deserializing weather information from cache: {}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public void cacheWeather(String key, WeatherInformation weatherInformation) {
        // Serialize weather information (using Jackson if needed)
        ObjectMapper objectMapper = new ObjectMapper(); // Example using Jackson
        String serializedWeather = null;
        try {
            serializedWeather = objectMapper.writeValueAsString(weatherInformation);
        } catch (JsonProcessingException e) {
            // Handle serialization exception
            e.printStackTrace();
        }
        if (serializedWeather != null) {
            String cacheKey = WEATHER_CACHE + ":" + key;
            redisTemplate.opsForValue().set(WEATHER_CACHE + ":" + key, serializedWeather);
            redisTemplate.expire(cacheKey, 60, TimeUnit.SECONDS); // Set expiration time (e.g., 60 seconds)
        }
        }
    }

