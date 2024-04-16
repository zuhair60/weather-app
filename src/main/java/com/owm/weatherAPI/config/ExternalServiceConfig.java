package com.owm.weatherAPI.config;

// A Java class using Spring Boot to configure properties for an external service.
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "external.service")
public class ExternalServiceConfig {

    private String openWeatherApiUrl;

}
