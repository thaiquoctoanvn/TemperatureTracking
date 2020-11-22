package com.example.temperaturetracking.data.repository

import com.example.temperaturetracking.data.entity.Date
import com.example.temperaturetracking.data.entity.TemperatureItem
import com.example.temperaturetracking.data.remote.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TemperatureRepository(private val apiService: APIService) {
    suspend fun getCurrentThreeDays(): Response<Date> {
        var response: Response<Date>
        withContext(Dispatchers.IO) {
            response = apiService.getCurrentThreeDays()
        }
        return response
    }

    suspend fun getTemperatureOfDate(date: String): Response<MutableList<TemperatureItem>> {
        var response: Response<MutableList<TemperatureItem>>
        withContext(Dispatchers.IO) {
            response = apiService.getTemperatureOfDate(date)
        }
        return response
    }

    suspend fun getLatestTemperature(): Response<TemperatureItem> {
        var response: Response<TemperatureItem>
        withContext(Dispatchers.IO) {
            response = apiService.getLatestTemperature()
        }
        return response
    }
}