package com.example.flightapp.InfoWindow

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.flightapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window_layout, null)

    private fun renderWindowText(marker: Marker, view: View) {
        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet
    }


    override fun getInfoContents(marker: Marker): View {
        renderWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View {
        renderWindowText(marker, mWindow)
        return mWindow
    }
}