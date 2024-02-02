package com.himanshudhiman1997.weatherforecast.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.himanshudhiman1997.weatherforecast.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

object FunctionUtils {

    //get day from the timestamp
    fun getDayFromTimestamp(timestamp: Long): String {
        try {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            val netDate = Date(timestamp * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            e.printStackTrace()
            return "N/A"
        }
    }

    fun kelvinToCelsiusString(kelvin: Double): Double {
        return kelvin - 273.15
    }

    //function to get the average temperature for each day
    fun calculateAverageTemperatures(data: List<Pair<String, Double>>): List<Map.Entry<String, Double>> {
        val tempMap = mutableMapOf<String, Double>()
        val countMap = mutableMapOf<String, Int>()

        for ((day, temperature) in data) {
            tempMap[day] = tempMap.getOrDefault(day, 0.0) + temperature
            countMap[day] = countMap.getOrDefault(day, 0) + 1
        }

        val mapResult = tempMap.mapValues { (day, totalTemp) ->
            totalTemp / countMap[day]!!
        }

        return mapResult.entries.toList().subList(1, 5)
    }

    fun showSnackBarWithAction(
        view: View,
        message: String,
        actionText: String,
        action: () -> Unit
    ) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction(actionText) {
                action.invoke()
            }
            .setActionTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.red
                )
            )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.show()
    }
}