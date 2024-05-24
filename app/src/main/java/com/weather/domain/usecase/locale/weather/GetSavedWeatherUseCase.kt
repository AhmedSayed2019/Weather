
package com.weather.domain.usecase.locale.weather

import com.weather.data.model.weathermodel.WeatherData
import com.weather.domain.repository.WeatherSharedPrefRepository
import javax.inject.Inject

class GetSavedWeatherUseCase @Inject constructor(private val repository: WeatherSharedPrefRepository) {

     operator fun invoke(): WeatherData? {
        return repository.getData()
    }

}

