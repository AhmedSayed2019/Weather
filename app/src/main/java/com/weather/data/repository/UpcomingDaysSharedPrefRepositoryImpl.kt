package com.weather.data.repository

import com.weather.data.local.UpcomingDaysSharedPrefService
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather
import com.weather.domain.repository.UpcomingDaysSharedPrefRepository

class UpcomingDaysSharedPrefRepositoryImpl (private val service: UpcomingDaysSharedPrefService):UpcomingDaysSharedPrefRepository{

   override fun sendData(weatherData: NextDaysWeather) {
        service.sendData(weatherData)
    }

    override  fun getData(): NextDaysWeather? {
        return service.getData()
    }

}