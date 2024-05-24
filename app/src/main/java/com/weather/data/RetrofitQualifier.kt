package com.weather.data

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Weather

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Location

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherByDayHour