package com.himanshudhiman1997.weatherforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val data: List<Map.Entry<String, Double>>) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.tv_forecast_day)
        val temperatureTextView: TextView = itemView.findViewById(R.id.tv_forecast_temp)
        val border: View = itemView.findViewById(R.id.view_border)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_forecast_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (day, temp) = data[position]
        holder.dayTextView.text = day

        holder.temperatureTextView.text =
            holder.itemView.resources.getString(R.string.temperature, String.format("%.0f", temp))

        //hiding the last border
        if (position == data.size - 1) {
            holder.border.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = data.size
}