package com.drmontoya.openweather.presentation.fragments.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drmontoya.openweather.R
import com.drmontoya.openweather.databinding.ItemSearchBinding
import com.drmontoya.openweather.domain.model.Location

class SearchResultsListAdapter(val onClick: (location: Location) -> Unit, val viewType: Int) :
    ListAdapter<Location, SearchResultViewHolder>(SearchResultItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position), onClick, viewType)
    }

    companion object {
        const val VIEW_TYPE_SAVED = 0
        const val VIEW_TYPE_RESULT = 1
    }
}

class SearchResultViewHolder
private constructor(val binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Location, onClick: (location: Location) -> Unit, viewType: Int) {
        binding.countryTextView.text = item.country
        binding.cityNameTextView.text = item.name
        if (viewType == SearchResultsListAdapter.VIEW_TYPE_SAVED) {
            binding.favoritesButton.setImageResource(R.drawable.ic_remove)
            if (item.name == SearchViewModel.CLEAR_ALL_ITEM) {
                binding.countryTextView.text = "Clear all items"
                binding.cityNameTextView.visibility = View.GONE
                binding.rankTextView.visibility = View.GONE
                binding.favoritesButton.setImageResource(R.drawable.ic_trash_can)
            }

        }
        binding.rankTextView.text =
            StringBuilder().append(binding.root.context.getString(R.string.population))
                .append(item.population)
        binding.favoritesButton.setOnClickListener { onClick(item) }
    }

    companion object {
        fun from(parent: ViewGroup): SearchResultViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchBinding.inflate(inflater, parent, false)
            return SearchResultViewHolder(binding)
        }
    }
}

class SearchResultItemDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return ((oldItem.latitude == newItem.latitude)
                && (oldItem.longitude == newItem.longitude))
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

}