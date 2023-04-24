package com.drmontoya.openweather.presentation.fragments.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drmontoya.openweather.databinding.ItemForecastDetailBinding
import com.drmontoya.openweather.domain.model.DetailHourlyForecast

class DetailListAdapter :
    ListAdapter<DetailHourlyForecast, DetailListViewHolder>(DetailListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailListViewHolder {
        return DetailListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DetailListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class DetailListViewHolder private constructor(val binding: ItemForecastDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DetailHourlyForecast) {
        binding.precipitationProbabilityTextView.text = item.precipitationProbability.toString()
        binding.temperatureTextView.text = item.temperature.toString()
        binding.dateTextView.text = item.date
    }

    companion object {
        fun from(parent: ViewGroup): DetailListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemForecastDetailBinding.inflate(inflater, parent, false)
            return DetailListViewHolder(binding)
        }
    }
}

class DetailListDiffUtil() : DiffUtil.ItemCallback<DetailHourlyForecast>() {
    override fun areItemsTheSame(
        oldItem: DetailHourlyForecast,
        newItem: DetailHourlyForecast
    ): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(
        oldItem: DetailHourlyForecast,
        newItem: DetailHourlyForecast
    ): Boolean {
        return oldItem == newItem
    }

}