package com.weather.data.remote

import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface WeatherByDayHourService {

    @GET("/v1/forecast")
    suspend fun getWeatherByDay(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("daily") dailyParameters: List<String>,
        @Query("timezone") timezone: String,
        @Query("forecast_days") forecastDays: Int
    ): Response<NextDaysWeatherModel>

    @GET("/v1/forecast")
    suspend fun getWeatherByHour(@Query("latitude") latitude: String, @Query("longitude") longitude: String, @Query("hourly") hourly: String, @Query("hourly") weatherCode: String, @Query("forecast_days") forecastDays: Int, @Query("timezone") timezone: String): Response<HourlyWeatherModel>

}