package com.weather.domain.repository

import com.weather.data.model.CurrentLocationData
import com.weather.data.model.weathermodel.WeatherData
import retrofit2.Response


interface LocationRepository {
    suspend fun getCurrentLocationData():Response<CurrentLocationData>
}

