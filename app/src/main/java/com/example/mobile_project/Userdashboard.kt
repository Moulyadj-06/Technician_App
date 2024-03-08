package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView

class Userdashboard : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdashboard)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showExitConfirmationDialog()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_user_details -> {
                // Start UserActivity when "User Details" is clicked
                startActivity(Intent(this, UsersActivity::class.java))
            }
            R.id.nav_logout -> {
                // Handle logout logic here
                // For example, show logout confirmation dialog
                showLogoutConfirmationDialog()
            }
        }
        return true
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

    private fun showLogoutConfirmationDialog() {
        // Implement logout confirmation dialog logic here
        // For example:
        AlertDialog.Builder(this)
            .setTitle("Logout Confirmation")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                // Implement logout logic here, like clearing session data, etc.
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
