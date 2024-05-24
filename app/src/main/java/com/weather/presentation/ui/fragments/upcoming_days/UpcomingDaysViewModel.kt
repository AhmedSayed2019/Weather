package com.weather.presentation.ui.fragments.upcoming_days

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.weather.core.viewstate.Resource
import com.weather.core.viewstate.Status
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeatherModel
import com.weather.domain.usecase.locale.upcoming_days.GetSavedUpcomingDaysUseCase
import com.weather.domain.usecase.locale.upcoming_days.SaveUpcomingDaysUseCase
import com.weather.domain.usecase.weather_by_day_hour.GetWeatherByDayUseCase
import com.weather.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingDaysViewModel @Inject constructor(
    private val getWeatherByDayUseCase: GetWeatherByDayUseCase,
    private val getSavedUpcomingDaysUseCase: GetSavedUpcomingDaysUseCase,
    private val saveUpcomingDaysUseCase: SaveUpcomingDaysUseCase,

    ) : BaseViewModel() {


    private val _weatherByDayLiveData = MutableLiveData<Resource<NextDaysWeatherModel>>()
    val weatherByDayLiveData: LiveData<Resource<NextDaysWeatherModel>> = _weatherByDayLiveData





    fun getWeatherByDay(latitude: String, longitude: String, dailyParameters: List<String>, timezone: String, forecastDays: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            val result  = getWeatherByDayUseCase.invoke(latitude, longitude, dailyParameters, timezone, forecastDays)

            if(result.isSuccessful && result.body() != null){
                _weatherByDayLiveData.postValue(Resource(Status.SUCCESS,  result.body() , null))
            }else{
                _weatherByDayLiveData.postValue(Resource(Status.ERROR,  null , "Error"))
            }
        }

    fun getLocaleWeatherByDay(): NextDaysWeather? {
        return getSavedUpcomingDaysUseCase.invoke()
    }


    fun saveWeatherByDay(weatherData: NextDaysWeather) =
        viewModelScope.launch(Dispatchers.IO) {
            saveUpcomingDaysUseCase.invoke(weatherData)
        }




}