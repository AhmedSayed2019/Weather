package com.weather.data.remote

import com.weather.data.model.CityLocationData
import com.weather.data.model.CurrentLocationData
import com.weather.data.model.weathermodel.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface LocationService {
    @GET("/json")
    suspend fun getCurrentLocationData(): Response<CurrentLocationData>
}