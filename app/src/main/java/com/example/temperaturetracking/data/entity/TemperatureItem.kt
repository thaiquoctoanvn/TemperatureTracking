package com.example.temperaturetracking.data.entity


import com.google.gson.annotations.SerializedName

data class TemperatureItem(
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("humidity")
    val humidity: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("tempeture")
    val temperature: String
)