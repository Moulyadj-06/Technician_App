package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CashPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment)

        // Retrieve the username passed from LoginActivity
        val username = intent.getStringExtra("USERNAME")

        // Retrieve the amount to pay from the intent extras
        val amountToPay = intent.getIntExtra("AMOUNT_TO_PAY", 150) // 150 is the default value if "AMOUNT_TO_PAY" is not found

        // Initialize the DBHelper
        val dbHelper = DBHelper(this)

        val buttonCash = findViewById<Button>(R.id.buttonConfirm)
        buttonCash.setOnClickListener {
            // Open the CashPaymentConfirmationActivity when the Cash button is clicked
            startActivity(Intent(this, Feedback::class.java))
            val safeUsername = username ?: "" // If username is null, use an empty string
            dbHelper.insertPaymentData(safeUsername, amountToPay, "Cash") // Store payment details in the database

        }

        // Update the TextView to display the amount using the string resource
        val textViewAmount = findViewById<TextView>(R.id.textViewAmount)
        textViewAmount.text = getString(R.string.amount_to_pay_format, amountToPay)
    }
}
