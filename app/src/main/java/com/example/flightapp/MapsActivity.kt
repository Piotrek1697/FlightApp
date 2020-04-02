package com.example.flightapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var searchView: SearchView
    private lateinit var searchButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        searchView = findViewById(R.id.searchView)
        searchButton = findViewById(R.id.searchActionButton)
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

    fun searchButtonAction(view: View) {
        ObjectAnimator.ofFloat(searchButton, View.ALPHA, 1f, 0f).setDuration(1000).start()
        //searchButton.visibility = View.INVISIBLE
        searchButton.isClickable = false

        searchView.isClickable = true
        searchView.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(searchView, View.ALPHA, 0f, 1f).setDuration(500).start()
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 700
        searchView.startAnimation(anim)
}

    fun searchViewAction(view: View){
        ObjectAnimator.ofFloat(searchView, View.ALPHA, 1f, 0f).setDuration(700).start()
        searchView.visibility = View.INVISIBLE
        searchView.isClickable = false

        searchButton.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(searchButton, View.ALPHA, 0f, 1f).setDuration(1000).start()
        val anim = ScaleAnimation(
            1.0f,
            0.0f,
            1.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 700
        searchView.startAnimation(anim)
        searchButton.isClickable = true
    }

}
