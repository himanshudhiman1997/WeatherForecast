package com.himanshudhiman1997.weatherforecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshudhiman1997.weatherforecast.model.ForecastResponse
import com.himanshudhiman1997.weatherforecast.model.WeatherResponse
import com.himanshudhiman1997.weatherforecast.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherData =
        MutableStateFlow<ApiResult<Pair<WeatherResponse, ForecastResponse>>?>(null)
    val weatherData: StateFlow<ApiResult<Pair<WeatherResponse, ForecastResponse>>?> get() = _weatherData

    fun getWeatherData(city: String) {
        viewModelScope.launch {
            try {
                weatherRepository.getWeatherData(city).collect {
                    _weatherData.value = ApiResult.Loading
                    _weatherData.value = it
                }

            } catch (e: Exception) {
                // Handle the exception
                _weatherData.value = ApiResult.Error("Something went wrong!!")
            }
        }
    }
}