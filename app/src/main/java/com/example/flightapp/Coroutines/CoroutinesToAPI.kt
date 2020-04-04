package com.example.flightapp.Coroutines

import android.content.Context
import android.os.Handler
import android.util.Log
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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


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
            //mMap.clear()
            setMarkersOnMap()
        }
    }

    //suspend fun delayMarkersOnMap() {
       // var cordsListPart: List<State>
       // var amount = cordsList.chunked(1)
      /*  for (x in 0..amount.size - 1) {
            cordsListPart = amount[x]
            //println("ELUWINA " + amount.size + " : " + cordsList.size + " : " + amount[amount.lastIndex].size)
            println("X" + x)
            delay(100)
            setMarkersOnMap(cordsList)
        }*/
    //}

    suspend fun setMarkersOnMap() {
        val plane = AirplaneVectorMarkers()

        cordsList.forEach {
            //Log.d("FlightState", it.toString())
            val cord = LatLng(it.latitude, it.longitude)
            mMap.addMarker(
                MarkerOptions().title("Country: " + it.origin_country).snippet("Velocity: " + it.velocity.toString() + " m/s").position(
                    cord
                ).rotation(it.true_track).icon(
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