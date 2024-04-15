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

        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPhoneNumber = findViewById<EditText>(R.id.edittextphonenumber)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val address = editTextAddress.text.toString()
            val username = editTextUsername.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()

            if (address.isEmpty()) {
                editTextAddress.error = "Address cannot be empty"
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                editTextUsername.error = "Username cannot be empty"
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
            val success = dbHelper.insertServiceRequest(address, username, phoneNumber)
            if (success) {
                Toast.makeText(this, "Data successfully submitted", Toast.LENGTH_SHORT).show()

                // Navigate to the Payment activity
                val intent = Intent(this, Payment::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to submit data", Toast.LENGTH_SHORT).show()
            }


            // Clear EditText fields after submission
            editTextAddress.text.clear()
            editTextUsername.text.clear()
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
