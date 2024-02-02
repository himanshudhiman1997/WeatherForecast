package com.himanshudhiman1997.weatherforecast.api

import com.himanshudhiman1997.weatherforecast.model.ForecastResponse
import com.himanshudhiman1997.weatherforecast.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(@Query("q") city: String, @Query("APPID") apiKey: String): WeatherResponse

    @GET("forecast")
    suspend fun getWeatherForecast(@Query("q") city: String, @Query("APPID") apiKey: String): ForecastResponse
}