package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        val loginButton = findViewById<Button>(R.id.btn_login)
        val editTextEmail = findViewById<EditText>(R.id.edit_email)
        val editTextPassword = findViewById<EditText>(R.id.edit_password)

        // Retrieve email and password from intent extras
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        // Set the retrieved email and password to the corresponding EditText fields
        editTextEmail.setText(email)
        editTextPassword.setText(password)

        loginButton.setOnClickListener {
            val enteredEmail = editTextEmail.text.toString().trim()
            val enteredPassword = editTextPassword.text.toString().trim()

            // Here you would implement your authentication logic,
            // such as validating credentials against a database or API
            // For demonstration purposes, let's assume email and password are correct

            // Validate email and password by querying the database
            if (isValidCredentials(enteredEmail, enteredPassword)) {
                // Successful login, navigate to the MainActivity
                Log.i("Login: ", "User successfully logged In")
                val intent = Intent(this, Userdashboard::class.java)
                startActivity(intent)
                finish()
            } else {
                // Invalid credentials, display a toast message
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        val dbHelper = DBHelper(this)

        findViewById<TextView>(R.id.forgot_password).setOnClickListener {
            // Implement forgot password functionality using SQLite
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.activity_forgot_password, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                resetPassword(userEmail.text.toString())
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        findViewById<TextView>(R.id.signupRedirectText).setOnClickListener {
            val signupIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(signupIntent)
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // Query the database to check if the entered email and password are valid
        val dbHelper = DBHelper(this)

        // Check if the entered credentials belong to an admin user
        if (email == "admin@example.com" && password == "admin000") {
            return true
        }

        // Check if the entered credentials belong to a regular user
        return dbHelper.checkCredentials(email, password)
    }

    private fun resetPassword(email: String) {
        // Implement logic to send password reset email
        // You can retrieve user information from the database based on the provided email
        // and then send a password reset email if the user exists
        // You can utilize dbHelper to interact with SQLite database for password reset
    }
}
