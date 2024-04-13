package com.example.mobile_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.location.Geocoder
import java.io.IOException

class Carservice : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carservice)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val buttonShowLocation = findViewById<Button>(R.id.buttonShowLocation)

        buttonShowLocation.setOnClickListener {
            val address = editTextAddress.text.toString()
            if (address.isNotEmpty()) {
                showLoading() // Show loading screen when the button is clicked
                updateMapWithAddress(address)
            } else {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        hideLoading() // Hide loading screen once the map is ready
        navigateToPaymentPage() // Navigate to payment page
    }

    private fun updateMapWithAddress(address: String) {
        val geoCoder = Geocoder(this)
        try {
            val locationList = geoCoder.getFromLocationName(address, 1)
            if (locationList != null) {
                if (locationList.isNotEmpty()) {
                    val location = locationList[0]
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val latLng = LatLng(latitude, longitude)
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(latLng).title(address))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } else {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error finding location", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLoading() {
        // Implement your loading screen logic here
    }

    private fun hideLoading() {
        // Implement your hiding loading screen logic here
    }

    private fun navigateToPaymentPage() {
        val intent = Intent(this, Payment::class.java)
        startActivity(intent)
        finish() // Finish the current activity
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
