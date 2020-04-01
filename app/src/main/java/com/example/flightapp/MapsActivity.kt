package com.example.flightapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val wroclawCords = LatLng(51.107883, 17.038538)
        val golCords = LatLng(53.564861, 14.827060)
        val koluszkiCords = LatLng(51.744240, 19.807680)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wroclawCords, 4.8f))
        mMap.addMarker(MarkerOptions().position(wroclawCords).title("Wroclaw marker"))
        mMap.addMarker(MarkerOptions().position(golCords).title("Goleniów marker"))
        mMap.addMarker(MarkerOptions().position(koluszkiCords).title("Koluszki  marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wroclawCords))
    }
}
