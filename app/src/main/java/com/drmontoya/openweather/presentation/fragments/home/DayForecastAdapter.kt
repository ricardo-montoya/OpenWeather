package com.drmontoya.openweather.presentation.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drmontoya.openweather.R
import com.drmontoya.openweather.databinding.ItemForecastBinding
import com.drmontoya.openweather.domain.model.DayForecastData

class DayForecastAdapter :
    ListAdapter<DayForecastData, DayForecastViewHolder>(DayForecastDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayForecastViewHolder {
        return DayForecastViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DayForecastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class DayForecastViewHolder private constructor(val binding: ItemForecastBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DayForecastData) {
        binding.temperatureTextView.text = buildMaxMinTempString(item)
        binding.dateTextView.text = formatePovidedDate(item)
        binding.forecastImageView.setImageResource(
            getWeatherStateImageFromItem(item)
        )
    }

    private fun getWeatherStateImageFromItem(item: DayForecastData): Int {
        return when (item.precipitaitonProbability) {
            in (0..20) -> R.drawable.sunny
            in (21..60) -> R.drawable.cloudy
            else -> R.drawable.breeze
        }
    }

    private fun formatePovidedDate(item: DayForecastData): String {
        val date = item.date.split('-')
        return "${date[1]} / ${date[2]}"
    }

    private fun buildMaxMinTempString(item: DayForecastData): String {
        return "${item.tempMin}°C - ${item.tempMax}°C"
    }

    companion object {
        fun from(parent: ViewGroup): DayForecastViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemForecastBinding.inflate(inflater, parent, false)
            return DayForecastViewHolder(binding)
        }
    }
}

class DayForecastDiffUtil() : DiffUtil.ItemCallback<DayForecastData>() {
    override fun areItemsTheSame(oldItem: DayForecastData, newItem: DayForecastData): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(
        oldItem: DayForecastData,
        newItem: DayForecastData
    ): Boolean {
        return oldItem == newItem
    }

}