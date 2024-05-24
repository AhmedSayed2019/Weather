package com.weather.presentation.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weather.core.viewstate.Resource
import com.weather.core.viewstate.Status
import com.weather.data.model.CurrentLocationData
import com.weather.data.model.nextweathermodel.hourlyweathers.HourlyWeatherModel
import com.weather.data.model.weathermodel.WeatherData
import com.weather.domain.usecase.locale.location.GetSavedLocationUseCase
import com.weather.domain.usecase.locale.location.SaveLocationUseCase
import com.weather.domain.usecase.locale.weather.GetSavedWeatherUseCase
import com.weather.domain.usecase.locale.weather.SaveWeatherUseCase
import com.weather.domain.usecase.location.GetCurrentLocationUseCase
import com.weather.domain.usecase.weather.GetWeatherUseCase
import com.weather.domain.usecase.weather_by_day_hour.GetWeatherByHourUseCase
import com.weather.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getWeatherByHourUseCase: GetWeatherByHourUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getSavedWeatherUseCase: GetSavedWeatherUseCase,
    private val savedWeatherUseCase: SaveWeatherUseCase,

    private val getSavedLocationUseCase: GetSavedLocationUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,
) : BaseViewModel() {


    private val _weatherLiveData = MutableLiveData<Resource<WeatherData>>()
    val weatherLiveData: LiveData<Resource<WeatherData>> = _weatherLiveData


    private val _currentLocationLiveData = MutableLiveData<Resource<CurrentLocationData>>()
    val currentLocationLiveData: LiveData<Resource<CurrentLocationData>> = _currentLocationLiveData


    private val _weatherByHourUseCaseLiveData = MutableLiveData<Resource<HourlyWeatherModel>>()
    val weatherByHourUseCaseLiveData: LiveData<Resource<HourlyWeatherModel>> = _weatherByHourUseCaseLiveData




    fun getWeather(lat: String, lon: String, appId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val result  = getWeatherUseCase.invoke(lat, lon, appId)

            if(result.isSuccessful && result.body() != null){
                _weatherLiveData.postValue(Resource(Status.SUCCESS,  result.body() , null))
            }else{
                _weatherLiveData.postValue(Resource(Status.ERROR,  null , "Error"))
            }
        }



    fun getCurrentLocation() =
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCurrentLocationUseCase.invoke()

            if(result.isSuccessful && result.body() != null){
                _currentLocationLiveData.postValue(Resource(Status.SUCCESS,  result.body() , null))
            }else{
                _currentLocationLiveData.postValue(Resource(Status.ERROR,  null , "Error"))
            }
        }


    fun getWeatherByHour(lat: String, lon: String, hourly: String, weatherCode: String, forecastDays: Int, timezone: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val result = getWeatherByHourUseCase.invoke(lat, lon, hourly, weatherCode, forecastDays, timezone)

            if(result.isSuccessful && result.body() != null){
                _weatherByHourUseCaseLiveData.postValue(Resource(Status.SUCCESS,  result.body() , null))
            }else{
                _weatherByHourUseCaseLiveData.postValue(Resource(Status.ERROR,  null , "Error"))
            }
        }

    fun getLocaleWeather(): WeatherData? {
        return getSavedWeatherUseCase.invoke()
    }


    fun saveWeather(weatherData: WeatherData) =
        viewModelScope.launch(Dispatchers.IO) {
            savedWeatherUseCase.invoke(weatherData)
        }

    fun getSavedLocation(): CurrentLocationData? {
        return getSavedLocationUseCase.invoke()
    }


    fun saveCurrentLocation(currentLocationData: CurrentLocationData) =
        viewModelScope.launch(Dispatchers.IO) {
            saveLocationUseCase.invoke(currentLocationData)
        }



}