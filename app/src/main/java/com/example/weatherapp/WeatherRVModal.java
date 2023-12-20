package com.example.weatherapp;

public class WeatherRVModal {
    // Deklarasi Variabel yang diperlukan

    private final String time;
    private final String temperature;
    private final String icon;
    private String windSpeed;

    // Membuat Constructor
    public WeatherRVModal(String time, String temperature, String icon, String windSpeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    // Membuat Getter
    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getIcon() {
        return icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

}
