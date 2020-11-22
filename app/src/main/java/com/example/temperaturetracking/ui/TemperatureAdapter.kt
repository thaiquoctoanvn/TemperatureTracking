package com.example.temperaturetracking.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.temperaturetracking.R
import com.example.temperaturetracking.data.entity.TemperatureItem
import com.example.temperaturetracking.util.FunctionalHelper
import kotlinx.android.synthetic.main.item_mini_temperature.view.*

class TemperatureAdapter : ListAdapter<TemperatureItem, TemperatureAdapter.TemperatureViewHolder>(TemperatureDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemperatureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TemperatureViewHolder(inflater.inflate(R.layout.item_mini_temperature, parent, false))
    }

    override fun onBindViewHolder(holder: TemperatureViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class TemperatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: TemperatureItem) {
            val time = FunctionalHelper.getHourFromMillis(item.createAt)
            val temperature = item.temperature
            itemView.tvTime.text = time
            itemView.tvMiniTemperature.text = temperature
        }
    }
}

class TemperatureDiffUtil : DiffUtil.ItemCallback<TemperatureItem>() {
    override fun areItemsTheSame(oldItem: TemperatureItem, newItem: TemperatureItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TemperatureItem, newItem: TemperatureItem): Boolean {
        return oldItem.temperature == newItem.temperature
    }
}