package com.weather.data.local

import android.content.Context
import com.google.gson.Gson
import com.weather.data.model.weathermodel.WeatherData
import com.weather.core.util.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherSharedPrefService @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getData() : WeatherData? {
        val data = sharedPref.getString(AppConstants.WEATHER_SHARED_PREF, null)
        return gson.fromJson(data, WeatherData::class.java)
    }

    fun sendData(weatherData: WeatherData) {
        val editor = sharedPref.edit()
        val data = gson.toJson(weatherData)
        editor.putString(AppConstants.WEATHER_SHARED_PREF, data)
        editor.apply()
    }

}