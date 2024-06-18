package com.example.homeshield

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttHelper(
    private val context: Context,
    private val brokerAddress: String,
    private val clientUser: String,
    private val clientPwd: String
) {

    private lateinit var mqttClient: MqttAndroidClient

    @SuppressLint("LogNotTimber")
    fun connect(callback: IMqttActionListener) {
        val clientId = MqttClient.generateClientId()
        mqttClient = MqttAndroidClient(context, "tcp://$brokerAddress", clientId)
        val connOptions = MqttConnectOptions().apply {
            userName = clientUser
            password = clientPwd.toCharArray()
        }

        try {
            mqttClient.connect(connOptions, null, callback)
        } catch (e: MqttException) {
            Log.d("MqttHelper", "Exception during connection")
            e.printStackTrace()
        }
    }

    fun setCallback(mqttCallback: MqttCallback) {
        mqttClient.setCallback(mqttCallback)
    }
    @SuppressLint("LogNotTimber")
    fun subscribe(topic: String, qos: Int, callback: IMqttActionListener) {
        try {
            mqttClient.subscribe(topic, qos, null, callback)
        } catch (e: MqttException) {
            Log.d("MqttHelper", "Subscription exception")
            e.printStackTrace()
        }
    }
    @SuppressLint("LogNotTimber")
    fun publish(topic: String, msg: String, qos: Int) {
        try {
            val mqttMessage = MqttMessage(msg.toByteArray(charset("UTF-8"))).apply {
                this.qos = qos
                isRetained = false
            }
            mqttClient.publish(topic, mqttMessage)
        } catch (e: Exception) {
            Log.d("MqttHelper", "Publishing exception")
            e.printStackTrace()
        }
    }
    @SuppressLint("LogNotTimber")
    fun disconnect(callback: IMqttActionListener) {
        try {
            mqttClient.disconnect(null, callback)
        } catch (e: MqttException) {
            Log.d("MqttHelper", "Disconnection exception")
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic: String) {
        mqttClient.unsubscribe(topic)
    }
}
