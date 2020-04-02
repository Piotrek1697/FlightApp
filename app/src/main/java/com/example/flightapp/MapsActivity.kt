package com.example.flightapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.flightapp.InfoWindow.CustomInfoWindowForGoogleMap
import com.example.flightapp.JsonFetch.JsonFetch
import com.example.flightapp.JsonFetch.State
import com.example.flightapp.Markers.AirplaneVectorMarkers
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var cordsList : MutableList<State>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        cordsList = JsonFetch.fetchJson()
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
        val plane = AirplaneVectorMarkers()
        // Add a marker in Sydney and move the camera
        val wroclawCords = LatLng(51.107883, 17.038538)
        val golCords = LatLng(53.564861, 14.827060)
        val koluszkiCords = LatLng(51.744240, 19.807680)
        cordsList.forEach {
            Log.d("FlightState",it.toString())
            val cord = LatLng(it.latitude,it.longitude)
            mMap.addMarker(MarkerOptions().title("Country: " + it.origin_country).snippet("Velocity: " + it.velocity.toString() + " m/s").position(cord).rotation(it.true_track).icon(plane.vectorMapDescriptor(applicationContext,R.drawable.ic_flight_black_24dp)))
            mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wroclawCords, 4.8f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wroclawCords))

    }





}
