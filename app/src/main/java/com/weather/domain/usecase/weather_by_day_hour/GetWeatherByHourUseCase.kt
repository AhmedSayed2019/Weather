package com.weather.domain.usecase.weather_by_day_hour

import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.domain.repository.WeatherByDayHourRepository
import retrofit2.Response
import javax.inject.Inject

class GetWeatherByHourUseCase @Inject constructor(private val repository: WeatherByDayHourRepository) {

    suspend operator fun invoke(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String): Response<HourlyWeatherModel> {
        return repository.getWeatherByHour(lat,lon,hourly,weatherCode,forecastDays,timezone)
    }
}