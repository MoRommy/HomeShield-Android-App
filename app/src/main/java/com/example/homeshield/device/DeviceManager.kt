package com.example.homeshield.device

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeviceManager {
    private val deviceService: DeviceService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.177/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        deviceService = retrofit.create(DeviceService::class.java)
    }

    fun getDevice(callback: Callback<DeviceData>) {
        val call = deviceService.getDevice()
        call.enqueue(callback)
    }

}