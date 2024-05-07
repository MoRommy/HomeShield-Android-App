package com.example.homeshield.weather

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherManager {
    private val weatherService: WeatherService
    private val apiKey = "K0r1j3FLuXdQExRro2D0HyUQzrW7ojzv"
    private var locationId: String = ""

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dataservice.accuweather.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherService = retrofit.create(WeatherService::class.java)
    }

    fun getDailyWeather(callback: Callback<DailyWeatherData>) {
        val call = weatherService.getDailyWeather(locationId, apiKey, true)
        call.enqueue(callback)
    }

    fun getCityId(city: String, callback: Callback<List<LocationResponse>>) {
        val call = weatherService.getLocation(apiKey, city)
        call.enqueue(callback)
    }

    fun setLocationId(id: String) {
        locationId = id
    }

    fun getLocationId(): String {
        return locationId
    }

}