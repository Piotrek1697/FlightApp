package com.example.flightapp

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.widget.TextView
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


open class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SearchView.OnQueryTextListener {

    private lateinit var mMap: GoogleMap

    private lateinit var searchView: SearchView
    private lateinit var searchButton: FloatingActionButton
    private lateinit var cordsList: MutableList<State>
    private var isFiltred: Boolean = false
    private lateinit var filterQuery: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //window.statusBarColor = Color.TRANSPARENT
        this.supportActionBar!!.hide()
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchView = findViewById(R.id.searchView)
        searchButton = findViewById(R.id.searchActionButton)

        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        val params = filterPerform(query)
        isFiltred = params.isFiltered
        if (params.isFiltered) {
            mMap.clear()
            putMarkersInSequence(mMap, params.statesList)
        } else
            Toast.makeText(applicationContext, params.statement, Toast.LENGTH_SHORT).show()
        appearAnimation(searchView, searchButton)
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val wroclawCords = LatLng(51.1078852, 17.0385376)
        //Set center on Wroclaw
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wroclawCords, 4.8f))
        CoroutineScope(IO).launch { coroutinesFetchJson() }
    }

    private suspend fun coroutinesFetchJson() {
        downloadJson()
        delay(30000)
        coroutinesFetchJson()
    }

    private suspend fun downloadJson() {
        cordsList = JsonFetch.fetchJson()
        if (isFiltred) {
            val params = filterPerform(filterQuery)
            setCordListInMainThread(params.statesList)
        } else {
            setCordListInMainThread(cordsList)
        }
    }

    private suspend fun setCordListInMainThread(list: MutableList<State>) {
        withContext(Dispatchers.Main) {
            mMap.clear()
            putMarkersOnMap(mMap, list)
        }
    }

    private fun putMarkersInSequence(mMap: GoogleMap, statesList: MutableList<State>) {
        CoroutineScope(Main).launch {
            putMarkersOnMap(mMap, statesList)
        }
    }

    private suspend fun putMarkersOnMap(mMap: GoogleMap, statesList: MutableList<State>) {
        val plane = AirplaneVectorMarkers()
        setAmountOfPlane(statesList.size)
        val filteredStatesList = statesList.filter { it.latitude != 0.0 && it.longitude != 0.0} as MutableList<State>
        filteredStatesList.forEachIndexed {i,it ->
            val cord = LatLng(it.latitude, it.longitude)
            mMap.addMarker(
                MarkerOptions().title(it.callsign)
                    .snippet(
                        "Country: ${it.origin_country}\n" +
                                "Altitude ${it.geo_altitude.toInt()} m\n" +
                                "Velocity: ${((it.velocity * 3.6).toInt()).toString()} km/h"
                    )
                    .position(cord).rotation(it.true_track).icon(
                        plane.vectorMapDescriptor(
                            applicationContext,
                            R.drawable.ic_flight_black_24dp
                        )
                    ).flat(true)
            )
            mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this))
            if (i%15 == 0)
                delay(1)
        }
    }


    fun allFlights(view: View) {
        mMap.clear()
        putMarkersInSequence(mMap, cordsList)
        isFiltred = false
        filterQuery = ""
    }

    fun searchButtonAction(view: View) {
        appearAnimation(view, searchView)
        showSoftKeyboard(searchView)
    }

    fun searchViewAction(view: View) {
        searchView.clearFocus()
        appearAnimation(searchView, searchButton)
    }

    private fun appearAnimation(disappearView: View, appearView: View) {
        ObjectAnimator.ofFloat(disappearView, View.ALPHA, 1f, 0f).setDuration(800).start()
        disappearView.isClickable = false
        appearView.isClickable = true
        appearView.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(appearView, View.ALPHA, 0f, 1f).setDuration(500).start()
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            1.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = 700
        appearView.startAnimation(anim)
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun isCountry(string: String): Boolean {
        var isCountry = false
        val l = Locale.ENGLISH
        Locale.getISOCountries().forEach {
            val locale = Locale("", it)
            val country = locale.getDisplayCountry(l)
            if (string.toLowerCase() == country.toLowerCase())
                isCountry = true
        }
        return isCountry
    }

    private fun filterPerform(query: String): FilterParams {
        var outMessage: String = ""
        var list: MutableList<State> = mutableListOf()
        var isFiltered: Boolean = false
        query.trim()
        filterQuery = query
        if (query != "") {
            var hasLetters = false
            var hasNumbers = false
            query.forEach {
                if (it.isLetter())
                    hasLetters = true
                if (it.isDigit())
                    hasNumbers = true
            }
            //Searching by flight number
            if (hasLetters && hasNumbers) {
                //mMap.clear()
                list =
                    cordsList.filter { it.callsign == query.toUpperCase() } as MutableList<State>
                if (list.size != 0)
                    isFiltered = true
                else {
                    outMessage = "There is no flights with number: ${query}"
                    isFiltered = false
                }
            } else if (isCountry(query)) { //Searching by country
                //mMap.clear()
                list =
                    cordsList.filter { it.origin_country.toLowerCase() == query.toLowerCase() } as MutableList<State>
                if (list.size != 0)
                    isFiltered = true
                else {
                    outMessage = "There is no flights with country: ${query}"
                    isFiltered = false
                }
            } else {
                outMessage = "Wrong query string"
                isFiltered = false
            }
        }

        searchView.setQuery("", false);
        searchView.clearFocus()
        return FilterParams(list, outMessage, isFiltered)
    }

    private fun setAmountOfPlane(amount: Int){
        val textView: TextView = findViewById(R.id.planeAmountView)
        textView.text = amount.toString()
    }
}

class FilterParams(
    val statesList: MutableList<State>,
    val statement: String,
    val isFiltered: Boolean
) {

}
