package com.weather.data.repository

import com.weather.data.local.LocationSharedPrefService
import com.weather.data.model.CurrentLocationData
import com.weather.domain.repository.LocationSharedPrefRepository

class LocationSharedPrefRepositoryImpl (private val service: LocationSharedPrefService) :LocationSharedPrefRepository{

   override fun getData(): CurrentLocationData? {
        return service.getData()
    }

    override  fun sendData(locationData: CurrentLocationData) {
        service.sendData(locationData)
    }

}