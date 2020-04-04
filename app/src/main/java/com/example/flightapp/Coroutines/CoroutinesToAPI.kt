package com.example.flightapp.Coroutines

import android.content.Context
import com.example.flightapp.InfoWindow.CustomInfoWindowForGoogleMap
import com.example.flightapp.JsonFetch.JsonFetch
import com.example.flightapp.JsonFetch.State
import com.example.flightapp.Markers.AirplaneVectorMarkers
import com.example.flightapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CoroutinesToAPI(val mMap: GoogleMap, val context: Context) {

    private lateinit var cordsList: MutableList<State>

    suspend fun coroutinesFetchJson() {
        println("Działa")
        downloadJson()
        delay(30000)
        coroutinesFetchJson()
    }

    private suspend fun downloadJson() {
        cordsList = JsonFetch.fetchJson()
        setCordListInMainThread()
    }

    private suspend fun setCordListInMainThread() {
        withContext(Dispatchers.Main) {
            mMap.clear()
            setMarkersOnMap()
        }
    }

    suspend fun setMarkersOnMap() {
        val plane = AirplaneVectorMarkers()

        cordsList.forEach {
            val cord = LatLng(it.latitude, it.longitude)
            cordsList.forEach {
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
                                context,
                                R.drawable.ic_flight_black_24dp
                            )
                        )
                )
                mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(context))
                delay(1)
            }
        }
    }
}