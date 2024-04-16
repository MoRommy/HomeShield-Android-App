package com.example.homeshield

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{true}
        Handler(Looper.getMainLooper()).postDelayed({
            splashScreen.setKeepOnScreenCondition{false}}, 2000)

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
    }
}
