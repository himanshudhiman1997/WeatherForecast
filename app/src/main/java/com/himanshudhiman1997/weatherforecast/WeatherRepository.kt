package com.himanshudhiman1997.weatherforecast

import android.util.Log
import com.himanshudhiman1997.weatherforecast.model.ForecastResponse
import com.himanshudhiman1997.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: WeatherApiService) {

    suspend fun getWeatherData(city: String): Flow<Pair<WeatherResponse, ForecastResponse>> = flow {
        try {
            val weatherResponse = apiService.getCurrentWeather(city, API_KEY)
            val forecastResponse = apiService.getWeatherForecast(city, API_KEY)

            emit(Pair(weatherResponse, forecastResponse))
        } catch (e: Exception) {
            // Handle exceptions here
            Log.e("WeatherRepository", "Error getting weather data: ${e.message}")
        }
    }

    companion object {
        private const val API_KEY = "9b8cb8c7f11c077f8c4e217974d9ee40"
    }
}