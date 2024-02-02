package com.himanshudhiman1997.weatherforecast

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModelToCollectData()

        mainViewModel.getWeatherData("Bengaluru")
    }

    private fun setUpViewModelToCollectData() {
        lifecycleScope.launch {
            mainViewModel.weatherData.collect { result ->
                // Handle the data
                if (result != null) {
                    val (currentWeather, forecast) = result
                    Log.d("Weather", "Current Weather: $currentWeather")
                    Log.d("Weather", "Forecast Weather: $forecast")
                }
            }
        }
    }
}