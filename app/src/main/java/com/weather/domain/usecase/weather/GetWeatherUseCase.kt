package com.weather.domain.usecase.weather

import com.weather.data.model.weathermodel.WeatherData
import com.weather.domain.repository.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(lat: String, lon: String, appId: String): Response<WeatherData> {
        return repository.getWeather(lat, lon, appId)
    }
}