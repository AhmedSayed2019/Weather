package com.weather.data

import com.weather.core.util.AppConstants
import com.weather.data.remote.LocationService
import com.weather.data.remote.WeatherByDayHourService
import com.weather.data.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherRetrofit {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60.toLong(), TimeUnit.SECONDS)
            .readTimeout(60.toLong(), TimeUnit.SECONDS)
            .writeTimeout(60.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor(Interceptor { chain ->
            try {
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")

//                 requestBuilder.addHeader("Authorization", "Bearer " + Your token)

                // Add other headers as needed
                val request = requestBuilder.build()
                chain.proceed(request)
            } catch (ex: Exception) {
                throw IOException(ex.message)
            }
        })

        return httpClient.build()
    }

    @Provides
    @Singleton
    @Weather
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(AppConstants.OpenWeatherMap_API_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @Weather
    fun provideApiService(@Weather retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    @Location
    fun provideLocationRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(AppConstants.CURRENT_LOCATION_API_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @Location
    fun provideLocationApiService(@Location retrofit: Retrofit): LocationService = retrofit.create(LocationService::class.java)

    @Provides
    @Singleton
    @WeatherByDayHour
    fun provideWeatherByDayHourRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(AppConstants.OpenMeteo_API_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @WeatherByDayHour
    fun provideWeatherByDayHourApiService(@WeatherByDayHour retrofit: Retrofit): WeatherByDayHourService = retrofit.create(WeatherByDayHourService::class.java)

}
