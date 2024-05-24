
package com.weather.domain.usecase.locale.location

import com.weather.data.model.CurrentLocationData
import com.weather.domain.repository.LocationSharedPrefRepository
import javax.inject.Inject

class GetSavedLocationUseCase @Inject constructor(private val repository: LocationSharedPrefRepository) {

     operator fun invoke(): CurrentLocationData? {
        return repository.getData()
    }

}

