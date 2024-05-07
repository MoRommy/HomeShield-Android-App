package com.example.homeshield

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.homeshield.device.Device
import com.example.homeshield.device.DeviceData
import com.example.homeshield.device.DeviceManager
import com.example.homeshield.weather.LocationResponse
import com.example.homeshield.workers.WeatherWorker
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val textView: TextView = findViewById(R.id.activityMainTextView)
        textView.text = getString(R.string.user).plus(user?.email)

        val signOutButton: Button = findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            finish()
            startActivity(intent)
        }








        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setInitialDelay(
            calculateInitialDelay(), TimeUnit.MILLISECONDS
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(25)
        ).setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.cancelUniqueWork("weatherWork")
        workManager.enqueue(workRequest)
        workManager.enqueueUniquePeriodicWork("weatherWork", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)




        val devices = mutableListOf<Device>()

        val deviceManager = DeviceManager()
        deviceManager.getDevice(object : Callback<DeviceData> {
            override fun onResponse(call: Call<DeviceData>, response: Response<DeviceData>) {
                response.body()?.let { devices.add(it.device) }
                Log.d("DeviceData", devices.toString())
            }

            override fun onFailure(call: Call<DeviceData>, t: Throwable) {
                Log.d("DeviceData", t.message.toString())
            }

        })




        val recyclerView: RecyclerView = findViewById(R.id.devicesRecyclerView)
        recyclerView.adapter = DeviceAdapter(devices)


















    }

    private fun calculateInitialDelay(): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        if (calendar.timeInMillis < now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis - now
    }

}
