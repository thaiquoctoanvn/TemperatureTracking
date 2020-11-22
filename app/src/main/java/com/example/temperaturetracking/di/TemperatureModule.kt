package com.example.temperaturetracking.di

import com.example.temperaturetracking.data.repository.TemperatureRepository
import com.example.temperaturetracking.ui.TemperatureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val temperatureRepository = module {
    single { TemperatureRepository(get()) }
}

val temperatureViewModel = module {
    viewModel { TemperatureViewModel(get()) }
}