package com.example.homeshield

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mqttClient: MqttAndroidClient
    private lateinit var lockUnlockButton: ImageView
    private lateinit var connectivityStatusImageView: ImageView
    private lateinit var deviceStatusTextView: TextView
    private var openCloseState = 0

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()


        val deviceIconImageView: ImageView = findViewById(R.id.deviceIconImageView)
        deviceIconImageView.setOnClickListener {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.deviceActivityFrame, DeviceHistoryFrame())
                    .commitNow()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.findFragmentById(R.id.deviceActivityFrame)
                if (fragment is BackPressHandler && fragment.onBackPressed()) {
                    // The fragment handled the back press event
                } else {
                    // Default back press behavior
                    finish()
                }
            }
        })

        val user = firebaseAuth.currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val userNameTextView: TextView = findViewById(R.id.activityMainTextView)
        userNameTextView.text = getString(R.string.user).plus(user?.email)

        val signOutButton: Button = findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            finish()
            startActivity(intent)
        }

        lockUnlockButton = findViewById(R.id.deviceCommandImageView)
        connectivityStatusImageView = findViewById(R.id.connectivityStatusImageView)
        deviceStatusTextView = findViewById(R.id.deviceStatusTextView)
        
        mqttConnect(this.applicationContext, "192.168.0.177", "", "")

        lockUnlockButton.setOnClickListener {
            if (openCloseState == 0) {
                mqttPublish("1001/open", "", 0)
            } else {
                mqttPublish("1001/close", "", 0)
            }
        }


    }



    fun mqttConnect(applicationContext: Context, brokeraddr: String, clientuser: String, clientpwd: String) {
        // ClientId is a unique id used to identify a client
        val clientId = MqttClient.generateClientId()

        Log.d("MqttClient", "Connecting")
        // Create an MqttAndroidClient instance
        mqttClient = MqttAndroidClient ( applicationContext, "tcp://$brokeraddr", clientId )


        // ConnectOption is used to specify username and password
        val connOptions = MqttConnectOptions()
//            connOptions.userName = clientuser
//            connOptions.password = clientpwd.toCharArray()


        try {
            // Perform connection
            mqttClient.connect(connOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Add here code executed in case of successful connection
                    Log.d("MqttClient", "Connection success")
                    mqttSubscribe("#", 0)
                    mqttSetReceiveListener()
                    mqttPublish("1001/get_device_status", "", 0)
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Add here code executed in case of failed connection
                    Log.d("MqttClient", "Connection failed")
                    exception.message?.let { Log.d("MqttClient", it) }
                }
            })
        } catch (e: MqttException) {
            // Get stack trace

            Log.d("MqttClient", "Exception")
            e.printStackTrace()
        }
    }

    fun mqttSetReceiveListener() {
        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                // Connection Lost
            }
            override fun messageArrived(topic: String, message: MqttMessage) {

                if (topic == "TM/1001/device_status") {
                    val data = String(message.payload, charset("UTF-8"))
                    val status = "Status: " + data.substring(10)
                    connectivityStatusImageView.setBackgroundResource(R.drawable.connected)
                    deviceStatusTextView.text = status

                    if (status == "Status: CLOSED") {
                        lockUnlockButton.setBackgroundResource(R.drawable.lock)
                        openCloseState = 0
                    } else {
                        lockUnlockButton.setBackgroundResource(R.drawable.unlock)
                        openCloseState = 1
                    }
                }

                // A message has been received
//                val data = String(message.payload, charset("UTF-8"))
                // Place the message into a specific TextBox object
//                Toast.makeText(this@MainActivity, "$topic: $data", Toast.LENGTH_SHORT).show()

            }
            override fun deliveryComplete(token: IMqttDeliveryToken) {
                // Delivery complete
            }
        })
    }

    fun mqttSubscribe(topic: String, qos: Int) {
        try {
            Log.d("MqttClient", "Subscribing to $topic")
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Successful subscribe
                    Log.d("MqttClient", "Subscription success")
                }override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Failed subscribe
                    Log.d("MqttClient", "Subscription failure")
                }
            })
        } catch (e: MqttException) {
            // Check error
            Log.d("MqttClient", "Subscription exception")
        }
    }

    fun mqttPublish(topic: String, msg: String, qos: Int) {
        try {
            val mqttMessage = MqttMessage(msg.toByteArray(charset("UTF-8")))
            mqttMessage.qos = qos
            mqttMessage.isRetained = false
            // Publish the message
            mqttClient.publish(topic, mqttMessage)
        } catch (e: Exception) {
            // Check exception
        }
    }



}
