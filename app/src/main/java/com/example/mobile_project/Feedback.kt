package com.example.mobile_project

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.content.ContextCompat

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        // Get the color you want to set
        val color = ContextCompat.getColor(this, R.color.your_color)

        // Create a ColorStateList with the desired color
        val colorStateList = ColorStateList.valueOf(color)

        // Set the color to the RatingBar
        ratingBar.setProgressTintList(colorStateList)

        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        // Initialize the DBHelper
        val dbHelper = DBHelper(this)

        buttonSubmit.setOnClickListener {
            val name = editTextName.text.toString()
            val rating = ratingBar.rating
            val description = editTextDescription.text.toString()

            // Validate input
            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertFeedbackData(name, rating, description)

                if (success) {
                    Toast.makeText(this, "FEEDBACK SUBMITTED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                    navigateToUserdashboard()
                } else {
                    Toast.makeText(this, "FAILED TO SUBMIT THE FEEDBACK", Toast.LENGTH_SHORT).show()
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
