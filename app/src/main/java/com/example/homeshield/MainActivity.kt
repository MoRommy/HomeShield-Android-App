package com.example.homeshield

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.homeshield.workers.WeatherWorker
import com.google.firebase.auth.FirebaseAuth
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

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
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(5)
        ).setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        ).build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork("weatherWork", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest)
        workManager.cancelUniqueWork("weatherWork")
    }
}
