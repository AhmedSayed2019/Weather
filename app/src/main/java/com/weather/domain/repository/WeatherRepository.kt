package com.weather.domain.repository

import com.weather.data.model.CityLocationData
import com.weather.data.model.weathermodel.WeatherData
import retrofit2.Response


interface WeatherRepository {
    suspend fun getWeather(lat: String, lon: String, appId: String): Response<WeatherData>

    suspend fun getGeoLocation(q: String, limit: Int, appId: String) :Response<CityLocationData>
}

