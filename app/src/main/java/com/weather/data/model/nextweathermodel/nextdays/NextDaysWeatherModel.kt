package com.weather.data.model.nextweathermodel.nextdays

data class NextDaysWeatherModel(
    val daily: NextDaysWeather,
    val daily_units: NextDaysWeatherUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)