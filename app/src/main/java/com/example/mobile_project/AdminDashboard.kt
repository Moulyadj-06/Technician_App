package com.example.mobile_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.content.ContextCompat

class AdminDashboard : AppCompatActivity() {

    private var listView: ListView? = null
    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        listView = findViewById(R.id.listView)
        dbHelper = DBHelper(this)

        displayServiceRequests()
    }

    private fun displayServiceRequests() {
        val serviceRequests: ArrayList<String> = dbHelper!!.getAllServiceRequests()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceRequests)
        listView!!.adapter = adapter
    }
}

