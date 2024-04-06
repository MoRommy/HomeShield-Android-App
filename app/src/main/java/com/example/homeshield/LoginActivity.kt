package com.example.homeshield

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        val registerTextView: TextView = findViewById(R.id.registerTextView)
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val signInButton: Button = findViewById(R.id.signInButton)

        signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val emailTextView: TextView = findViewById(R.id.emailAddressEditText)
        val passwordTextView: TextView = findViewById(R.id.passwordEditText)

        var emailAddress = emailTextView.text.toString()
        var password = passwordTextView.text.toString()

        if (emailAddress.isNotEmpty() && password.isNotEmpty()) {

            if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                firebaseAuth.signInWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Sign in succeeded!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, it.result.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show()
        }
    }
}
