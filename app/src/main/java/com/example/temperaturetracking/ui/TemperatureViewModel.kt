package com.example.temperaturetracking.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.temperaturetracking.data.entity.Date
import com.example.temperaturetracking.data.entity.TemperatureItem
import com.example.temperaturetracking.data.repository.TemperatureRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch

class TemperatureViewModel(private val temperatureRepository: TemperatureRepository) : ViewModel() {

    private val history = MutableLiveData<MutableList<String>>()
    private val currentThreeDays = MutableLiveData<Date>()
    private val temperatureOfDate = MutableLiveData<MutableList<TemperatureItem>>()
    private val latestTemperature = MutableLiveData<TemperatureItem>()

    fun getHistoryValue() = history
    fun getCurrentThreeDaysValue() = currentThreeDays
    fun getTemperatureOfDateValue() = temperatureOfDate
    fun getLatestTemperatureValue() = latestTemperature

    fun registerToFCM() {
        viewModelScope.launch {
            FirebaseMessaging.getInstance().subscribeToTopic("test")
            Firebase.messaging.token.addOnCompleteListener(OnCompleteListener {
                if (!it.isSuccessful) {
                    Log.w("###", "Fetching FCM registration token failed", it.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = it.result
                // Log and toast
                Log.d("###", token.toString())
            })
        }
    }

    fun getCurrentThreeDays() {
        viewModelScope.launch {
            val res = temperatureRepository.getCurrentThreeDays().body()
            if(res != null) {
                currentThreeDays.value = res
            }
        }
    }

    suspend fun getTemperatureOfDate(position: Int) {
        viewModelScope.launch {
            var date = ""
            if(currentThreeDays.value != null) {
                date = when(position) {
                    0 -> currentThreeDays.value!!.today
                    1 -> currentThreeDays.value!!.yesterday
                    2 -> currentThreeDays.value!!.twoDayAgo
                    else -> {
                        Log.d("###", "Error when try to get data by date")
                        return@launch
                    }
                }
                val res = temperatureRepository.getTemperatureOfDate(date).body()
                if(res != null) {
                    temperatureOfDate.value = res
                }
            }
        }
    }

    fun getLatestTemperature() {
        viewModelScope.launch {
            val res = temperatureRepository.getLatestTemperature().body()
            if(res != null) {
                latestTemperature.value = res
            }
        }
    }
}