
# OpenWeatherMap API and Redis Cache Integration with Spring Boot

This Spring Boot application integrates the OpenWeatherMap API to fetch weather data for a given city and utilizes Redis caching to store and retrieve weather information efficiently.

## Features

- **Weather Information Retrieval:** Users can query the application with a city name to fetch real-time weather information.
- **Redis Caching:** Weather data fetched from the OpenWeatherMap API is cached using Redis to improve performance and reduce API calls.
- **RESTful API:** The application provides a RESTful API endpoint to retrieve weather information by city name.

## Technologies Used

- **Spring Boot:** For building the application.
- **OpenWeatherMap API:** To fetch weather data for specified cities.
- **Redis:** For caching weather information.
- **Maven:** Dependency management.

## App Usage

To retrieve weather information for a city, make a GET request to `/get-weather` endpoint with the `cityName` parameter.

Example: GET http://localhost:8080/get-Weather?cityName=London


## Configuration

Ensure you have configured your OpenWeatherMap API key in the `com.owm.weatherAPI.utility.Constants` calss file:
