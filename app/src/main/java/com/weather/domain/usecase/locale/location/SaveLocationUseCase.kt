
package com.weather.domain.usecase.locale.location

import com.weather.data.model.CurrentLocationData
import com.weather.domain.repository.LocationSharedPrefRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(private val repository: LocationSharedPrefRepository) {

     operator fun invoke(currentLocationData: CurrentLocationData){
        return repository.sendData(currentLocationData)
    }

}

