package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mobile_project.DBHelper
import com.example.mobile_project.R

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val editTextName = findViewById<EditText>(R.id.edit_name)
        val editTextEmail = findViewById<EditText>(R.id.edit_email)
        val editTextPassword = findViewById<EditText>(R.id.edit_password)
        val editTextConfirmPassword = findViewById<EditText>(R.id.edit_confirm_password)
        val buttonRegister = findViewById<Button>(R.id.btn_register)
        val textViewAlreadyHaveAccount = findViewById<TextView>(R.id.text_already_have_account)

        // Initialize the DBHelper
        val dbHelper = DBHelper(this)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val confirmPassword = editTextConfirmPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Save user credentials securely (e.g., SharedPreferences, local database)
                val success = dbHelper.insertUserdata(name, email, password)
                if (success) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Navigate to the login page
                    navigateToLogin()
                } else {
                    Toast.makeText(this, "Failed to register user", Toast.LENGTH_SHORT).show()
                }
            }
        }

        textViewAlreadyHaveAccount.setOnClickListener {
            // Navigate to the login page
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        // Navigate to the login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
