package com.weather.domain.usecase.weather_by_day_hour

import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeatherModel
import com.weather.domain.repository.WeatherByDayHourRepository
import retrofit2.Response
import javax.inject.Inject

class GetWeatherByDayUseCase @Inject constructor(private val repository: WeatherByDayHourRepository) {

    suspend operator fun invoke(
        latitude: String,
        longitude: String,
        dailyParameters: List<String>,
        timezone: String,
        forecastDays: Int
    ): Response<NextDaysWeatherModel> {
        return repository.getWeatherByDay(latitude, longitude, dailyParameters, timezone, forecastDays)
    }
}