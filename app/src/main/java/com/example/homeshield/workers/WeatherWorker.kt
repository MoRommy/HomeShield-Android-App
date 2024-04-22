package com.example.homeshield.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.homeshield.weather.LocationResponse
import com.example.homeshield.weather.Weather
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import com.example.homeshield.weather.DailyWeatherData


class WeatherWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("WeatherWorkerPreferences", Context.MODE_PRIVATE)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("WeatherData", "Hello")

        val weather = getWeather(applicationContext)
        val dailyWeather = getDailyWeather(weather)

        val res = checkDailyWeather(dailyWeather)
        Log.d("WeatherData", "res: $res")
        Result.success()
    }

    private fun checkDailyWeather(dailyWeather: DailyWeatherData?): Boolean {
        if (dailyWeather == null)
            return false
        return dailyWeather.dailyForecasts[0].temperature.minimum.value >= 9 &&
               dailyWeather.dailyForecasts[0].temperature.maximum.value >= 27 &&
               dailyWeather.dailyForecasts[0].day.icon <=7 &&
               !dailyWeather.dailyForecasts[0].day.hasPrecipitation
    }

    private fun getDailyWeather(weather: Weather): DailyWeatherData? {
        var data: DailyWeatherData? = null
        weather.getDailyWeather(object : Callback<List<DailyWeatherData>> {
            override fun onResponse(
                call: Call<List<DailyWeatherData>>,
                response: Response<List<DailyWeatherData>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherData: List<DailyWeatherData>? = response.body()
                        Log.d("WeatherData", "Res: $it")
                        if (weatherData != null) {
                            data = weatherData[0]
                        }
                    }
                } else {
                    Log.d("WeatherData", "ERROR: ${response.errorBody()?.toString()}")
                }
            }

            override fun onFailure(call: Call<List<DailyWeatherData>>, t: Throwable) {
                Log.d("WeatherData", "FAILURE: ${t.message}")
            }

        })
        return data
    }

    private fun createWeather(): Weather {
        val weather = Weather()
        weather.getLocation("bucharest", object : Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherData = response.body()
                        weatherData?.get(0)?.key?.let { it1 -> weather.setLocationId(it1) }
                        Log.d("WeatherData", "Res: $it")
                        setWeather(weather)
                    }
                } else {
                    Log.d("WeatherData", "ERROR: ${response.errorBody()?.toString()}")
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                Log.d("WeatherData", "FAILURE: ${t.message}")
            }
        })
        return weather
    }

    private fun getWeather(context: Context): Weather {
        val sharedPreferences = context.getSharedPreferences("WeatherWorkerPrefs", Context.MODE_PRIVATE)

        // Retrieve the JSON string
        val weather = sharedPreferences.getString("weather", null)

        // Deserialize the JSON back to UserProfile
        return if (weather != null) {
            val gson = Gson()
            gson.fromJson(weather, Weather::class.java)
        } else {
            return createWeather()
        }
    }

    fun setWeather(weather: Weather) {
        val editor = sharedPreferences.edit()

        // Serialize the user profile to JSON
        val gson = Gson()
        val weatherString = gson.toJson(weather)

        // Store the serialized JSON
        editor.putString("weather", weatherString)
        editor.apply()
    }



}
