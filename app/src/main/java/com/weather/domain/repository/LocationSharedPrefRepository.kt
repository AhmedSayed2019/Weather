package com.weather.domain.repository

import com.weather.data.model.CurrentLocationData

interface LocationSharedPrefRepository {
     fun getData(): CurrentLocationData?
     fun sendData(locationData: CurrentLocationData)
}

