package com.example.flightapp.JsonFetch

import okhttp3.*
import java.io.IOException

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
            }

        })
    }

}