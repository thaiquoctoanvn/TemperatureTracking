package com.example.temperaturetracking.data.remote

import com.example.temperaturetracking.data.entity.Date
import com.example.temperaturetracking.data.entity.TemperatureItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("getLatestTemperature.php")
    suspend fun getLatestTemperature(): Response<TemperatureItem>

    @GET("push.php")
    suspend fun getCurrentThreeDays(): Response<Date>

    @GET("test.php")
    suspend fun getTemperatureOfDate(@Query("date") dateInUnix: String): Response<MutableList<TemperatureItem>>
}