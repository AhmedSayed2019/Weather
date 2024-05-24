package com.weather.domain.repository

import com.weather.data.model.weathermodel.WeatherData


interface WeatherSharedPrefRepository {
    fun getData(): WeatherData?
    fun sendData(weatherData: WeatherData)
}


