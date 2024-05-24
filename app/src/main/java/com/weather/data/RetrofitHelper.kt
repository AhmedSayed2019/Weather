package com.weather.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getInstance(baseURL: String) : Retrofit {
        return Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}
//
//fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
//
//fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//
//
//    return Retrofit.Builder().baseUrl("http://udeal-eg.com/api/").client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
//}