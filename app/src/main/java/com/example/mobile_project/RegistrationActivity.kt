package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
                showToast("Please fill in all fields")
            } else if (!isValidEmail(email)) {
                showToast("Invalid email format")
            } else if (password != confirmPassword) {
                showToast("Passwords do not match")
            } else if (!isValidPassword(password)) {
                showToast("Password must be at least 6 characters long")
            } else {
                // Save user credentials securely (e.g., SharedPreferences, local database)
                val success = dbHelper.insertUserdata(name, email, password)
                if (success) {
                    showToast("Registration successful")
                    navigateToLogin()
                } else {
                    showToast("Failed to register user")
                }
            }
        }

        textViewAlreadyHaveAccount.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



}
