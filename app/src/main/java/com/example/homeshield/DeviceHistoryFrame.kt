package com.example.homeshield

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class DeviceHistoryFrame(private val mqttHelper: MqttHelper) : Fragment(), BackPressHandler {

    private val dataset: MutableList<Event> = mutableListOf()
    private lateinit var customAdapter: DeviceHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frame_device_history, container, false)

        // Dataset for the RecyclerView
        customAdapter = DeviceHistoryAdapter(dataset)

        // RecyclerView setup
        val recyclerView: RecyclerView = view.findViewById(R.id.deviceHistoryRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = customAdapter

        // Whitelist setup
        val editWhitelistButton: Button = view.findViewById(R.id.editWhitelistButton)
        editWhitelistButton.setOnClickListener {
            if (savedInstanceState == null) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.whitelistFrame, WhitelistFrame(mqttHelper))
                    .commitNow()
            }
        }

        mqttHelper.setCallback(createMqttCallback())
        mqttHelper.publish("1001/get_device_history", "", 0)

        return view
    }

    override fun onBackPressed(): Boolean {
        // Check if the child fragment is handling the back press
        val childFragment = childFragmentManager.findFragmentById(R.id.whitelistFrame)
        if (childFragment is BackPressHandler && childFragment.onBackPressed()) {
            return true
        }

        // Handle the back button press logic here for the parent fragment
        parentFragmentManager.beginTransaction().remove(this).commit()
        return true // Indicate that the back press event has been handled
    }

    private fun createMqttCallback(): MqttCallback {
        return object : MqttCallback {
            @SuppressLint("LogNotTimber")
            override fun connectionLost(cause: Throwable?) {
                Log.d("MqttClient - DeviceHistory", "Connection lost")
            }
            override fun deliveryComplete(token: IMqttDeliveryToken?) {}

            @SuppressLint("LogNotTimber")
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("MqttClient - DeviceHistory", "Message arrived " + "size: " + message.toString().length + " | " + topic)
                if (topic == "TM/1001/device_history") {
                    val event = parseEvent(message.toString())
                    if (event != null) {
                        event.timestamp = event.timestamp.dropLast(7)
                        dataset.add(event)
                        customAdapter.notifyItemChanged(dataset.size - 1)
                    }
                }
            }
        }
    }

    fun parseEvent(jsonString: String): Event? {
        return try {
            Json.decodeFromString<Event>(jsonString)
        } catch (e: SerializationException) {
            e.printStackTrace()
            null
        }
    }

}
