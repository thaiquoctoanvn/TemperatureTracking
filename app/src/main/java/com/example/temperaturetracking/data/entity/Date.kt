package com.example.temperaturetracking.data.entity


import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("today")
    val today: String,
    @SerializedName("twoDayAgo")
    val twoDayAgo: String,
    @SerializedName("yesterday")
    val yesterday: String
)