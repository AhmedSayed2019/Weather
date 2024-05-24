package com.weather.data.repository

import com.weather.data.model.CurrentLocationData
import com.weather.data.remote.LocationService
import com.weather.domain.repository.LocationRepository
import retrofit2.Response

class LocationRepositoryImpl(private val service: LocationService) :LocationRepository{

    override suspend fun getCurrentLocationData(): Response<CurrentLocationData> {
        return service.getCurrentLocationData()
    }

}