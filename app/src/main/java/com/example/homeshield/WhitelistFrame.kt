package com.example.homeshield

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class WhitelistFrame(private val mqttHelper: MqttHelper) : Fragment(), BackPressHandler {

    private lateinit var whitelistTextView: TextView
    private lateinit var refreshWhitelistButton: Button
    private lateinit var addPlateIdButton: Button
    private lateinit var removePlateIdButton: Button
    private lateinit var plateNumberEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frame_whitelist, container, false)

        mqttHelper.setCallback(createMqttCallback())
        mqttHelper.publish("1001/get_whitelist", "", 0)

        whitelistTextView = view.findViewById(R.id.whitelistTextView)
        refreshWhitelistButton = view.findViewById(R.id.refreshWhitelistButton)
        addPlateIdButton = view.findViewById(R.id.addPlateIdButton)
        removePlateIdButton = view.findViewById(R.id.removePlateIdButton)
        plateNumberEditText = view.findViewById(R.id.plateNumberEditText)

        refreshWhitelistButton.setOnClickListener {
            mqttHelper.publish("1001/get_whitelist", "", 0)
        }

        addPlateIdButton.setOnClickListener {
            if (verifyPlateNumberEditText()) {
                mqttHelper.publish("1001/add_to_whitelist", plateNumberEditText.text.toString(), 0)
                mqttHelper.publish("1001/get_whitelist", "", 0)
            }
        }

        removePlateIdButton.setOnClickListener {
            if (verifyPlateNumberEditText()) {
                mqttHelper.publish("1001/remove_from_whitelist", plateNumberEditText.text.toString(), 0)
                mqttHelper.publish("1001/get_whitelist", "", 0)
            }
        }


        return view
    }

    private fun verifyPlateNumberEditText(): Boolean {
        return plateNumberEditText.text.toString().length >= 6
    }


    override fun onBackPressed(): Boolean {
        parentFragmentManager.beginTransaction().remove(this).commit()
        return true
    }

    private fun createMqttCallback(): MqttCallback {
        return object : MqttCallback {
            @SuppressLint("LogNotTimber")
            override fun connectionLost(cause: Throwable?) {
                Log.d("MqttClient - Whitelist", "Connection lost")
            }
            override fun deliveryComplete(token: IMqttDeliveryToken?) {}

            @SuppressLint("LogNotTimber")
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d("MqttClient - Whitelist", "Message arrived" + topic + message.toString())
                if (topic == "TM/1001/whitelist") {
                    val whitelist = transformStringToArray(message.toString())
                    var result = ""
                    for (id in whitelist)
                        result += id + "\n\n"

                    whitelistTextView.text = result
                }
            }
        }
    }

    fun transformStringToArray(input: String): Array<String> {
        // Remove the surrounding square brackets and single quotes, then split by comma
        return input.removeSurrounding("[", "]")
            .split(", ")
            .map { it.trim('\'') }
            .toTypedArray()
    }



}
