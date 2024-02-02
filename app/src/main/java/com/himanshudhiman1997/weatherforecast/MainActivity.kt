package com.himanshudhiman1997.weatherforecast

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshudhiman1997.weatherforecast.databinding.ActivityMainBinding
import com.himanshudhiman1997.weatherforecast.model.ForecastItem
import com.himanshudhiman1997.weatherforecast.model.ForecastResponse
import com.himanshudhiman1997.weatherforecast.model.WeatherResponse
import com.himanshudhiman1997.weatherforecast.utils.ApiResult
import com.himanshudhiman1997.weatherforecast.utils.FunctionUtils.calculateAverageTemperatures
import com.himanshudhiman1997.weatherforecast.utils.FunctionUtils.getDayFromTimestamp
import com.himanshudhiman1997.weatherforecast.utils.FunctionUtils.kelvinToCelsiusString
import com.himanshudhiman1997.weatherforecast.utils.FunctionUtils.showSnackBarWithAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //changing the status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)


        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

        val rootView: View = findViewById(android.R.id.content)

        setUpViewModelToCollectData(activityMainBinding, rootView)

        mainViewModel.getWeatherData("Bengaluru")
    }

    private fun setUpViewModelToCollectData(
        activityMainBinding: ActivityMainBinding,
        rootView: View
    ) {
        lifecycleScope.launch {
            mainViewModel.weatherData.collect { result ->
                // Handle the data
                when (result) {
                    is ApiResult.Success -> {
                        // Handle successful response
                        val data = result.data
                        // Update UI with data

                        val (currentWeather, forecastWeather) = data

                        sendDataToUI(currentWeather, forecastWeather, activityMainBinding)
                    }

                    is ApiResult.Error -> {
                        // Handle error
                        val errorMessage = result.message
                        // Show error message in the UI - SnackBar
                        errorMessage?.let {
                            showSnackBarWithAction(
                                rootView,
                                getString(R.string.something_went_wrong), getString(R.string.retry)
                            ) {
                                mainViewModel.getWeatherData("Bengaluru")
                            }
                        }
                        activityMainBinding.progressBar.visibility = View.GONE
                    }

                    is ApiResult.Loading -> {
                        // Show loading indicator
                        toggleView(activityMainBinding, true)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun sendDataToUI(
        currentWeather: WeatherResponse,
        forecastWeather: ForecastResponse,
        activityMainBinding: ActivityMainBinding
    ) {
        //current weather
        activityMainBinding.tvCurrentCity.text = currentWeather.name

        val currentTemp =
            getString(
                R.string.temperature,
                String.format("%.0f", kelvinToCelsiusString(currentWeather.main.temp))
            )
        activityMainBinding.tvCurrentTemp.text = currentTemp


        //forecast weather
        val forecastList = prepareData(forecastWeather.list)

        val adapter = WeatherAdapter(forecastList)
        activityMainBinding.rvForecastData.adapter = adapter
        activityMainBinding.rvForecastData.layoutManager = LinearLayoutManager(this)

        //show ui
        toggleView(activityMainBinding, false)
    }

    private fun prepareData(list: List<ForecastItem>): List<Map.Entry<String, Double>> {
        val forecastList: ArrayList<Pair<String, Double>> = ArrayList()
        for (item in list) {
            forecastList.add(
                Pair(
                    getDayFromTimestamp(item.dt),
                    kelvinToCelsiusString(item.main.temp)
                )
            )
        }

        val distinctDays = forecastList.map { it.first }.distinct()

        return calculateAverageTemperatures(forecastList.filter { it.first in distinctDays.take(5) })
    }

    private fun toggleView(activityMainBinding: ActivityMainBinding, showProgressBar: Boolean) {
        if (showProgressBar) {
            activityMainBinding.progressBar.visibility = View.VISIBLE
            activityMainBinding.dataHolderView.visibility = View.GONE
        } else {
            activityMainBinding.progressBar.visibility = View.GONE
            activityMainBinding.dataHolderView.visibility = View.VISIBLE

            addAnimationToForecastCardView(activityMainBinding)
        }
    }

    private fun addAnimationToForecastCardView(activityMainBinding: ActivityMainBinding) {
        // Set the initial translationY to move it off-screen
        activityMainBinding.cvForecastData.translationY = 1000f

        // Create an ObjectAnimator for translationY
        val translateYAnimator = ObjectAnimator.ofFloat(
            activityMainBinding.cvForecastData,
            "translationY",
            0f
        )

        // Set the animation duration
        translateYAnimator.duration = 1000

        translateYAnimator.start()
    }
}