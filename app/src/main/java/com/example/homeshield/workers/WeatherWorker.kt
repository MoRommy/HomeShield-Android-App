package com.example.homeshield.workers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.homeshield.weather.DailyWeatherData
import com.example.homeshield.weather.LocationResponse
import com.example.homeshield.weather.WeatherManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WeatherWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("WeatherWorkerPreferences", Context.MODE_PRIVATE)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Log.d("WeatherData", "Hello")

        val weather = getWeather()
        Log.d("WeatherData", "weather: $weather")

        if (weather.getLocationId() == "") {
            Log.d("WeatherData", "unavailable location id")
            weather.getCityId("bucharest", object : Callback<List<LocationResponse>> {
                override fun onResponse(
                    call: Call<List<LocationResponse>>,
                    response: Response<List<LocationResponse>>
                ) {
                    Log.d("WeatherData", "getCityId response: ${response.body()}")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val weatherData = response.body()
                            weatherData?.get(0)?.key?.let { it1 -> weather.setLocationId(it1) }
                            Log.d("WeatherData", "got weather: $it")
                            serializeWeather(weather)
                            checkDailyWeather(weather)
                        }
                    } else {
                        Log.d("WeatherData", "ERROR - getCityId: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                    Log.d("WeatherData", "FAILURE - getCityId: ${t.message}")
                }
            })
        } else {
            checkDailyWeather(weather)
        }
        Result.success()
    }

    private fun checkDailyWeather(weather: WeatherManager) {
        Log.d("WeatherData", "Checking daily weather")
        weather.getDailyWeather(object : Callback<DailyWeatherData> {
            override fun onResponse(
                call: Call<DailyWeatherData>,
                response: Response<DailyWeatherData>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherData: DailyWeatherData? = response.body()
                        Log.d("WeatherData", "Got daily weather: $it")
                        if (weatherData != null) {
                            val isGoodWeather = checkDailyWeatherConditions(weatherData)
                            Log.d("WeatherData", "SUCCESS! Time for a walk: $isGoodWeather")
                        }
                    }
                } else {
                    Log.d("WeatherData", "ERROR - checkDailyWeather: ${response.errorBody()?.toString()}")
                }
            }

            override fun onFailure(call: Call<DailyWeatherData>, t: Throwable) {
                Log.d("WeatherData", "FAILURE - checkDailyWeather: ${t.message}")
            }

        })
    }

    private fun checkDailyWeatherConditions(dailyWeather: DailyWeatherData?): Boolean {
        Log.d("WeatherData", "Checking daily weather conditions")
        if (dailyWeather == null)
            return false
        return (dailyWeather.dailyForecasts[0].temperature.minimum.value >= 9) &&
                (dailyWeather.dailyForecasts[0].temperature.maximum.value >= 27) &&
                (dailyWeather.dailyForecasts[0].day.icon <= 7) &&
                (!dailyWeather.dailyForecasts[0].day.hasPrecipitation)
    }

    private fun getWeather(): WeatherManager {
        // Retrieve the JSON string
        val weather = sharedPreferences.getString("weather", null)

        // Deserialize the JSON back to UserProfile
        return if (weather != null) {
            Log.d("Serializer", "Instance deserialized")
            val gson = Gson()
            gson.fromJson(weather, WeatherManager::class.java)
        } else {
            Log.d("Serializer", "New instance created")
            WeatherManager()
        }
    }

    fun serializeWeather(weather: WeatherManager) {
        val editor = sharedPreferences.edit()

        // Serialize the user profile to JSON
        val gson = Gson()
        val weatherString = gson.toJson(weather)

        // Store the serialized JSON
        editor.putString("weather", weatherString)
        editor.apply()
        Log.d("Serializer", "Instance serialized")
    }

}
