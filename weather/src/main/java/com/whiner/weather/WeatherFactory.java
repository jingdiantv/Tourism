package com.whiner.weather;

public interface WeatherFactory {

    void getData(OnWeatherListener onWeatherListener);

    void stopGet();

}
