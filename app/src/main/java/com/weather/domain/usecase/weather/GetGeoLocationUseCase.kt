
package com.weather.domain.usecase.weather

import com.weather.data.model.CityLocationData
import com.weather.domain.repository.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class GetGeoLocationUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend operator fun invoke(q: String, limit: Int, appId: String): Response<CityLocationData> {
        return repository.getGeoLocation(q, limit, appId)
    }

}

