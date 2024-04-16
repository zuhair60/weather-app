package com.owm.weatherAPI.DTO;

public class WeatherInformation {
    private String currentWeather;
    private double temperature;
    private double windSpeed;

    public WeatherInformation() {
        // Default constructor required for Jackson deserialization
    }

    public WeatherInformation(String currentWeather, double temp, double windSpeed) {
        this.currentWeather = currentWeather;
        this.temperature = temp;
        this.windSpeed = windSpeed;
    }


    public String getCurrentWeather() {
        return currentWeather;
    }


    public double getTemperature() {
        return temperature;
    }


    public double getWindSpeed() {
        return windSpeed;
    }
}

