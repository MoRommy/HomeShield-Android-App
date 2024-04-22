package com.example.homeshield.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("locations/v1/cities/search")
    fun getLocation(
        @Query("apikey") apiKey: String,
        @Query("q") city: String
    ): Call<List<LocationResponse>>

    @GET("forecasts/v1/daily/1day/{location_id}")
    fun getDailyWeather(
        @Path("location_id") locationId: String,
        @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean  // true for Celsius, false for Fahrenheit
    ): Call<List<DailyWeatherData>>

}