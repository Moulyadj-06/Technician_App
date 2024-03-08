package com.example.mobile_project

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory



class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the support action bar
        supportActionBar?.hide()
        // Change status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        setContentView(R.layout.activity_welcome)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            // Retrieve registration details passed from RegistrationActivity
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            // Navigate to LoginActivity with registration details
            val intent = Intent(this, LoginActivity::class.java).apply {
                putExtra("email", email)
                putExtra("password", password)
            }
            startActivity(intent)
            finish() // Finish WelcomeActivity to prevent going back to it after login
        }

        // Example of navigating to the registration screen when a button is clicked
        val registerButton = findViewById<Button>(R.id.registerButton);
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }



    }


}
