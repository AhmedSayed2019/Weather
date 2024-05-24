package com.weather.data.repository

import com.weather.data.model.CityLocationData
import com.weather.data.model.weathermodel.WeatherData
import com.weather.data.remote.WeatherService
import com.weather.domain.repository.WeatherRepository
import retrofit2.Response

class WeatherRepositoryImpl(private val service: WeatherService) :WeatherRepository{

    override suspend fun getWeather(lat: String, lon: String, appId: String): Response<WeatherData> {
        return service.getWeather(lat, lon, appId)
    }

    override suspend fun getGeoLocation(
        q: String,
        limit: Int,
        appId: String
    ): Response<CityLocationData> {
        return service.getGeoLocation(q, limit, appId)
    }

}