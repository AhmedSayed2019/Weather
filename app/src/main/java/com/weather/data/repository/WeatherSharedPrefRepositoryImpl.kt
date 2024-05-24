package com.weather.data.repository

import com.weather.data.local.WeatherSharedPrefService
import com.weather.data.model.weathermodel.WeatherData
import com.weather.domain.repository.WeatherSharedPrefRepository

class WeatherSharedPrefRepositoryImpl (private val service: WeatherSharedPrefService) :WeatherSharedPrefRepository{

    override fun getData(): WeatherData? {
        return service.getData()
    }

    override fun sendData(weatherData: WeatherData) {
        service.sendData(weatherData)
    }

}