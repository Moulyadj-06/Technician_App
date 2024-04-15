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


        val buttonCash = findViewById<Button>(R.id.buttonConfirm)
        buttonCash.setOnClickListener {
            // Open the CashPaymentConfirmationActivity when the Cash button is clicked
            startActivity(Intent(this, Feedback::class.java))

        }

        // Retrieve the amount to pay from the intent extras
        val amountToPay = intent.getIntExtra("AMOUNT_TO_PAY", 150) // 0 is the default value if "AMOUNT_TO_PAY" is not found

        // Update the TextView to display the amount using the string resource
        val textViewAmount = findViewById<TextView>(R.id.textViewAmount)
        textViewAmount.text = getString(R.string.amount_to_pay_format, amountToPay)
    }
}