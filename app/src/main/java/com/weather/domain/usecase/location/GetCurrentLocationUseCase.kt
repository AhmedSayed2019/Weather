
package com.weather.domain.usecase.location

import com.weather.data.model.CurrentLocationData
import com.weather.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(private val repository: LocationRepository) {

    suspend operator fun invoke(): Response<CurrentLocationData> {
        return repository.getCurrentLocationData()
    }

}

