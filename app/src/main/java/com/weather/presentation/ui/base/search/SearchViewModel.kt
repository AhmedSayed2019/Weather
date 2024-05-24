package com.weather.presentation.ui.base.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weather.core.viewstate.Resource
import com.weather.core.viewstate.Status
import com.weather.data.model.CityLocationData
import com.weather.data.model.CurrentLocationData
import com.weather.domain.usecase.locale.location.GetSavedLocationUseCase
import com.weather.domain.usecase.locale.location.SaveLocationUseCase
import com.weather.domain.usecase.weather.GetGeoLocationUseCase
import com.weather.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getGeoLocationUseCase: GetGeoLocationUseCase,
    private val getSavedLocationUseCase: GetSavedLocationUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,

    ) : BaseViewModel() {



    private val _geoLocationLiveData = MutableLiveData<Resource<CityLocationData>>()
    val geoLocationLiveData: LiveData<Resource<CityLocationData>> = _geoLocationLiveData


    fun getGeoLocation(q: String, limit: Int, appId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val result  = getGeoLocationUseCase.invoke(q, limit, appId)

            if(result.isSuccessful && result.body() != null){
                _geoLocationLiveData.postValue(Resource(Status.SUCCESS,  result.body() , null))
            }else{
                _geoLocationLiveData.postValue(Resource(Status.ERROR,  null , "Error"))
            }
        }

    fun getSavedLocation(): CurrentLocationData? {
        return getSavedLocationUseCase.invoke()
    }


    fun saveLocation(data: CurrentLocationData) =
        viewModelScope.launch(Dispatchers.IO) {
            saveLocationUseCase.invoke(data)
        }




}