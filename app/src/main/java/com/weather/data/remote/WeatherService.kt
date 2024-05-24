package com.weather.data.remote

import com.weather.data.model.CityLocationData
import com.weather.data.model.CurrentLocationData
import com.weather.data.model.weathermodel.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherService {
    @GET("/data/2.5/weather")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appId: String): Response<WeatherData>

    @GET("/geo/1.0/direct")
    suspend fun getGeoLocation(@Query("q") q: String, @Query("limit") limit: Int, @Query("appid") appId: String) : Response<CityLocationData>
}