package com.example.flightapp.JsonFetch

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class JsonFetch {

    fun fetchJson() {

        val url = "https://opensky-network.org/api/states/all?fbclid=IwAR1obM0u5xI5RTXPSk1ssa_zbXtLnl1xFSW79azne1TaHs0uwVMTEB7SKys"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
            println(body)

            val gson = GsonBuilder().create()
            val allFlights = gson.fromJson(body, AllFlights::class.java)
            val info = GetInfoForMarkers(allFlights)
                println("Origin Country " + info.getOriginCountry(1))
        }
        })
            }
}

class AllFlights(val states:List<List<String>>) {
}





