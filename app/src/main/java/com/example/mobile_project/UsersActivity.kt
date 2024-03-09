package com.example.mobile_project

import android.content.Intent
import  android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobile_project.DBHelper

class UsersActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        dbHelper = DBHelper(this)

        val editTextName = findViewById<EditText>(R.id.edit_name)
        val editTextEmail = findViewById<EditText>(R.id.edit_email)
        val editTextPassword = findViewById<EditText>(R.id.edit_password)

        // Add Create button
        findViewById<Button>(R.id.btn_create).setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val success = dbHelper.insertUserdata(name, email, password)
                if (success) {
                    showToast("User created successfully")
                    clearEditTexts(editTextName, editTextEmail, editTextPassword)
                } else {
                    showToast("Failed to create user")
                }
            } else {
                showToast("Please fill in all fields")
            }
        }

        // Add Read button
        findViewById<Button>(R.id.btn_read).setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                val user = dbHelper.getUserByEmail(email)
                if (user != null) {
                    showToast("User details: Name - ${user.name}, Email - ${user.email}, Password - ${user.password}")
                } else {
                    showToast("User not found")
                }
            } else {
                showToast("Please enter an email")
            }
        }

        // Add Update button
        findViewById<Button>(R.id.btn_update).setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = dbHelper.getUserByEmail(email) // Fetch user details first
                if (user != null) {
                    val success = dbHelper.updateUser(user.id, name, email, password)
                    if (success) {
                        showToast("User details updated successfully")
                    } else {
                        showToast("Failed to update user details")
                    }
                } else {
                    showToast("User not found")
                }
            } else {
                showToast("Please fill in all fields")
            }
        }

        // Add Delete button
        findViewById<Button>(R.id.btn_delete).setOnClickListener {
            val email = editTextEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                val success = dbHelper.deleteUserByEmail(email)
                if (success) {
                    showToast("User deleted successfully")
                    clearEditTexts(editTextName, editTextEmail, editTextPassword)
                } else {
                    showToast("Failed to delete user")
                }
            } else {
                showToast("Please enter an email")
            }
        }

        // Add Save button
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            val success = dbHelper.insertUserdata(name, email, password)
            if (success) {
                showToast("Details saved successfully")
                startActivity(Intent(this, Userdashboard::class.java))
                finish() // Finish current activity
            } else {
                showToast("Failed to save details")
            }
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearEditTexts(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }
}
