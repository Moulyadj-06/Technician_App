package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class Carservice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carservice)

        // Initialize the DBHelper
        val dbHelper = DBHelper(this)

        val editTextUsername = findViewById<EditText>(R.id.edittextusername)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val editTextProblem = findViewById<EditText>(R.id.editTextProblem)
        val editTextPhoneNumber = findViewById<EditText>(R.id.edittextphonenumber)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val username = editTextUsername.text.toString()
            val address = editTextAddress.text.toString()
            val problem = editTextProblem.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()

            if (username.isEmpty()) {
                editTextUsername.error = "Username cannot be empty"
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                editTextAddress.error = "Address cannot be empty"
                return@setOnClickListener
            }

            if (problem.isEmpty()) {
                editTextProblem.error = "Problem cannot be empty"
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                editTextPhoneNumber.error = "Phone number cannot be empty"
                return@setOnClickListener
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                editTextPhoneNumber.error = "Invalid phone number"
                return@setOnClickListener
            }

            // Insert data into the database
            val success = dbHelper.insertServiceRequest(username, address, problem, phoneNumber)
            if (success) {
                Toast.makeText(this, "Data successfully submitted", Toast.LENGTH_SHORT).show()

                // Navigate to the Payment activity
                val intent = Intent(this, Payment::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to submit data", Toast.LENGTH_SHORT).show()
            }

            // Clear EditText fields after submission
            editTextUsername.text.clear()
            editTextAddress.text.clear()
            editTextProblem.text.clear()
            editTextPhoneNumber.text.clear()
        }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Regular expression to match exactly 10 digits
        val pattern = Pattern.compile("^\\d{10}$")
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
}
