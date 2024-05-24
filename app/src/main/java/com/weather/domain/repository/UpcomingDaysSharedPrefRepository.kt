package com.weather.domain.repository

import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather

interface UpcomingDaysSharedPrefRepository {
     fun sendData(weatherData: NextDaysWeather)
     fun getData(): NextDaysWeather?
}



