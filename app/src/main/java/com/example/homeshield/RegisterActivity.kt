package com.example.homeshield

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()
        var singUpButton: Button = findViewById(R.id.signUpButton)
        singUpButton.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        var emailAddressElement: EditText = findViewById(R.id.emailAddressEditText)
        var passwordElement: EditText = findViewById(R.id.passwordEditText)
        var confirmPasswordElement: EditText = findViewById(R.id.confirmPasswordEditText)

        var emailAddress = emailAddressElement.text.toString()
        var password = passwordElement.text.toString()
        var confirmPassword = confirmPasswordElement.text.toString()

        if (emailAddress.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() &&
                    password.length >= 6) {
                    firebaseAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Register complete!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, it.result.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show()
        }
    }
}