package com.example.mobile_project

import User
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class Userdashboard : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdashboard)

        // Retrieve the username passed from LoginActivity
        val username = intent.getStringExtra("username")

        // Display the greeting with the username
        val greetingText = "Welcome!!, " +
                "$username"
        findViewById<TextView>(R.id.textViewGreeting).text = greetingText



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle bottom navigation item clicks here.
        when (item.itemId) {

            R.id.nav_home ->{
                // Start UsersActivity when "User Details" is clicked
                startActivity(Intent(this, Userdashboard::class.java))
                return true}

            R.id.nav_user_details -> {
                // Start UsersActivity when "User Details" is clicked
                startActivity(Intent(this, UsersActivity::class.java))
                return true
            }
            R.id.nav_logout -> {
                // Handle logout logic here
                // For example, show logout confirmation dialog
                showExitConfirmationDialog()
                return true
            }
        }
        return false
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit Confirmation")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                // Start WelcomeActivity
                startActivity(Intent(this, WelcomeActivity::class.java))
                // Finish the current activity
                finishAffinity() // This will exit the app
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}