package com.weather.data.repository

import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeatherModel
import com.weather.data.remote.WeatherByDayHourService
import com.weather.domain.repository.WeatherByDayHourRepository
import retrofit2.Response

class WeatherByDayHourRepositoryImpl(private val service: WeatherByDayHourService) :WeatherByDayHourRepository{
    override suspend fun getWeatherByHour(
        lat: String,
        lon: String,
        hourly: String,
        weatherCode: String,
        forecastDays: Int,
        timezone: String
    ): Response<HourlyWeatherModel> {
        return service.getWeatherByHour(lat,lon,hourly, weatherCode, forecastDays, timezone)
    }

    override suspend fun getWeatherByDay(
        latitude: String,
        longitude: String,
        dailyParameters: List<String>,
        timezone: String,
        forecastDays: Int
    ): Response<NextDaysWeatherModel> {
        return service.getWeatherByDay(latitude, longitude, dailyParameters, timezone, forecastDays)
    }


}