
package com.weather.domain.usecase.locale.upcoming_days

import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather
import com.weather.domain.repository.UpcomingDaysSharedPrefRepository
import javax.inject.Inject

class GetSavedUpcomingDaysUseCase @Inject constructor(private val repository: UpcomingDaysSharedPrefRepository) {

     operator fun invoke(): NextDaysWeather? {
        return repository.getData()
    }

}

