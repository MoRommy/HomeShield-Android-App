package com.example.homeshield.device

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeviceService {
    @GET("/api/get_device")
    fun getDevice(
//        @Query("apikey") apiKey: String,
//        @Query("q") city: String
    ): Call<DeviceData>

}