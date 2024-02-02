package com.himanshudhiman1997.weatherforecast.ui.home.repository

import android.util.Log
import com.himanshudhiman1997.weatherforecast.BuildConfig
import com.himanshudhiman1997.weatherforecast.api.WeatherApiService
import com.himanshudhiman1997.weatherforecast.model.ForecastResponse
import com.himanshudhiman1997.weatherforecast.model.WeatherResponse
import com.himanshudhiman1997.weatherforecast.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: WeatherApiService) {

    suspend fun getWeatherData(city: String): Flow<ApiResult<Pair<WeatherResponse, ForecastResponse>>> =
        flow {
            try {
                val weatherResponse = apiService.getCurrentWeather(city, BuildConfig.weatherAPIKey)
                val forecastResponse =
                    apiService.getWeatherForecast(city, BuildConfig.weatherAPIKey)

                emit(ApiResult.Success(Pair(weatherResponse, forecastResponse)))
            } catch (e: Exception) {
                // Handle exceptions here
                emit(ApiResult.Error("Error getting weather data: ${e.message}"))
                Log.e("WeatherRepository", "Error getting weather data: ${e.message}")
            }
        }
}