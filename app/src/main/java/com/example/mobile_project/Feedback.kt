package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        // Initialize the DBHelper
        val dbHelper = DBHelper(this)

        buttonSubmit.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val rating = ratingBar.rating

            // Validate input
            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertFeedbackData(name, description, rating)

                if (success) {
                    Toast.makeText(this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show()
                    navigateToUserdashboard()
                } else {
                    Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToUserdashboard() {
        val intent = Intent(this, Userdashboard::class.java)
        startActivity(intent)
        finish() // Finish the current activity so that the user cannot go back to it
    }
}
