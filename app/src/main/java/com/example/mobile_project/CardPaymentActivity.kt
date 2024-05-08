package com.example.mobile_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import android.widget.Toast
import java.util.Calendar
import java.util.Locale

class CardPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment)

        val editTextCardNumber = findViewById<EditText>(R.id.editTextCardNumber)
        val editTextExpiryDate = findViewById<EditText>(R.id.editTextExpiryDate)
        val editTextCVV = findViewById<EditText>(R.id.editTextCVV)
        val buttonSubmitPayment = findViewById<Button>(R.id.buttonSubmitPayment)

        // Retrieve the username passed from LoginActivity
        val username = intent.getStringExtra("USERNAME")

        buttonSubmitPayment.setOnClickListener {
            val cardNumber = editTextCardNumber.text.toString()
            val expiryDate = editTextExpiryDate.text.toString()
            val cvv = editTextCVV.text.toString()

            // Retrieve the amount to pay from the intent extras
            val amountToPay = intent.getIntExtra("AMOUNT_TO_PAY", 150) // 150 is the default value if "AMOUNT_TO_PAY" is not found

            // Initialize the DBHelper
            val dbHelper = DBHelper(this)

            if (validateCardNumber(cardNumber) && validateExpiryDate(expiryDate) && validateCVV(cvv)) {
                // Payment successful
                Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()

                // Navigate to feedback screen
                startActivity(Intent(this, Feedback::class.java))
                val safeUsername = username ?: "" // If username is null, use an empty string
                dbHelper.insertPaymentData(safeUsername, amountToPay, "Cash") // Store payment details in the database
                finish() // Finish this activity to prevent going back to it from the feedback screen
            } else {
                Toast.makeText(this, "Invalid card details", Toast.LENGTH_SHORT).show()
            }
        }
        // Retrieve the amount to pay from the intent extras
        val amountToPay = intent.getIntExtra("AMOUNT_TO_PAY", 150) // 0 is the default value if "AMOUNT_TO_PAY" is not found

        // Update the TextView to display the amount using the string resource
        val textViewAmount = findViewById<TextView>(R.id.textViewAmount)
        textViewAmount.text = getString(R.string.amount_to_pay_format, amountToPay)
    }

    private fun validateCardNumber(cardNumber: String): Boolean {
        // Check if the card number has exactly 12 digits
        return cardNumber.length == 12
    }

    private fun validateExpiryDate(expiryDate: String): Boolean {
        // Check if expiry date is in the format MM/YY and represents a future date
        val dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
        dateFormat.isLenient = false // To enforce strict parsing

        try {
            val currentDate = Calendar.getInstance().time
            val parsedDate = dateFormat.parse(expiryDate)

            // Ensure parsedDate is not null and represents a future date
            return parsedDate != null && parsedDate.after(currentDate)
        } catch (e: Exception) {
            return false // If parsing or validation fails
        }
    }

    private fun validateCVV(cvv: String): Boolean {
        // Check if the CVV has exactly 3 digits
        return cvv.length == 3
    }
}
