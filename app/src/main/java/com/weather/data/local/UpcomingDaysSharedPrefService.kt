package com.weather.data.local

import android.content.Context
import com.google.gson.Gson
import com.weather.data.model.nextweathermodel.nextdays.NextDaysWeather
import com.weather.core.util.AppConstants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpcomingDaysSharedPrefService @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPref = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun sendData(weatherData: NextDaysWeather) {
        val data = gson.toJson(weatherData)
        val editor = sharedPref.edit()
        editor.putString(AppConstants.NEXT_SEVEN_WEATHER_SHARED_PREF, data)
        editor.apply()
    }

    fun getData(): NextDaysWeather? {
        val data = sharedPref.getString(AppConstants.NEXT_SEVEN_WEATHER_SHARED_PREF, null)
        return gson.fromJson(data, NextDaysWeather::class.java)
    }

}