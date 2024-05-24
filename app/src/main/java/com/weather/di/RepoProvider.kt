package com.weather.di

import com.weather.data.Location
import com.weather.data.Weather
import com.weather.data.WeatherByDayHour
import com.weather.data.local.LocationSharedPrefService
import com.weather.data.local.UpcomingDaysSharedPrefService
import com.weather.data.local.WeatherSharedPrefService
import com.weather.data.remote.*

import com.weather.data.repository.*
import com.weather.domain.repository.*

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoProvider {

    //remote

    @Provides
    @Singleton
    fun provideWeatherRepo(@Weather service: WeatherService): WeatherRepository = WeatherRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideLocationRepo(@Location service: LocationService): LocationRepository = LocationRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideWeatherByDayHourRepo(@WeatherByDayHour service: WeatherByDayHourService): WeatherByDayHourRepository = WeatherByDayHourRepositoryImpl(service)

    //locale

    @Provides
    @Singleton
    fun provideUpcomingDaysSharedPrefRepo(service: UpcomingDaysSharedPrefService): UpcomingDaysSharedPrefRepository = UpcomingDaysSharedPrefRepositoryImpl(service)


    @Provides
    @Singleton
    fun provideLocationSharedPrefRepo( service: LocationSharedPrefService): LocationSharedPrefRepository = LocationSharedPrefRepositoryImpl(service)


    @Provides
    @Singleton
    fun provideWeatherSharedPrefRepo( service: WeatherSharedPrefService): WeatherSharedPrefRepository = WeatherSharedPrefRepositoryImpl(service)




}
