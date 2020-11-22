package com.example.temperaturetracking.di

import com.example.temperaturetracking.data.remote.APIService
import com.example.temperaturetracking.util.ConstantHelper
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

val apiModule = module {
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(httpLoggingInterceptor)
        }
        // Return an OkHttpClient
        return okHttpClientBuilder.build()
    }

    fun provideNullConverter(): Converter.Factory {
        return object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) = if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
            }
        }
    }

    fun provideRetrofit(okHttpClient: OkHttpClient, nullConverter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConstantHelper.SERVER_API_URL)
            .client(okHttpClient)
            .addConverterFactory(nullConverter)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    fun provideAPIService(retrofit: Retrofit) = retrofit.create(APIService::class.java)

    single { provideOkHttpClient() }
    single { provideNullConverter() }
    single { provideRetrofit(get(), get()) }
    single { provideAPIService(get()) }
}