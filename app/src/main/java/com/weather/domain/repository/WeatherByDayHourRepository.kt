package com.weather.domain.repository

import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeatherModel
import retrofit2.Response


interface WeatherByDayHourRepository {
    suspend fun getWeatherByHour(
        lat: String,
        lon: String,
        hourly: String,
        weatherCode: String,
        forecastDays: Int,
        timezone: String
    ): Response<HourlyWeatherModel>

    suspend fun getWeatherByDay(
        latitude: String,
        longitude: String,
        dailyParameters: List<String>,
        timezone: String,
        forecastDays: Int
    ): Response<NextDaysWeatherModel>
}

