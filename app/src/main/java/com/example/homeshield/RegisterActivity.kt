package com.example.homeshield

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        val singUpButton: Button = findViewById(R.id.signUpButton)
        singUpButton.setOnClickListener {
            signUp()
        }

        val loginTextView: TextView = findViewById(R.id.loginTextView)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun signUp() {
        val emailAddressElement: EditText = findViewById(R.id.emailAddressEditText)
        val passwordElement: EditText = findViewById(R.id.passwordEditText)
        val confirmPasswordElement: EditText = findViewById(R.id.confirmPasswordEditText)

        val emailAddress = emailAddressElement.text.toString()
        val password = passwordElement.text.toString()
        val confirmPassword = confirmPasswordElement.text.toString()

        checkCredentials(emailAddress, password, confirmPassword)
    }

    private fun checkCredentials(
        emailAddress: String,
        password: String,
        confirmPassword: String
    ) {
        if (emailAddress.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Register complete!", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(
                                this,
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show()
        }
    }
}