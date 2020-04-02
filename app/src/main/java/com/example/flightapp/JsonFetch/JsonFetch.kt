package com.example.flightapp.JsonFetch

import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch

class JsonFetch {

    companion object {
        fun fetchJson(): MutableList<State> {
            val url =
                "https://opensky-network.org/api/states/all?fbclid=IwAR1obM0u5xI5RTXPSk1ssa_zbXtLnl1xFSW79azne1TaHs0uwVMTEB7SKys"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            val countDownLatch = CountDownLatch(1)
            var list = mutableListOf<State>()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Failed to execute request")
                    countDownLatch.countDown()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()

                    val gson = GsonBuilder().create()
                    val allFlights = gson.fromJson(body, AllFlights::class.java)
                    list = parseToState(allFlights)
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
            return list
        }

        private fun parseToState(flights: AllFlights): MutableList<State> {
            val list = mutableListOf<State>()
            flights.states.forEach { list.add(getState(it)) }
            return list
        }

        private fun getState(state: Array<String?>): State {
            val floatIndexes = listOf<Int>(5, 6, 9, 10)
            if (state[3] == "" || state[3] == "null" || state[3] == null)
                state[3] = "0"
            floatIndexes.forEach {
                if (state[it] == "" || state[it] == "null" || state[it] == null)
                    state[it] = "0.0"
            }
            return State(
                state[2]!!,
                state[3]!!.toInt(),
                state[5]!!.toDouble(),
                state[6]!!.toDouble(),
                state[9]!!.toFloat(),
                state[10]!!.toFloat()
            )
        }
    }


}

// KLASY WRZUCIŁEM NA RAZIE TU, EBY BYŁO ŁATWIEJ
class AllFlights(val states: MutableList<Array<String?>>) { // <-- JEŻELI TU DAMY STRING ZAMIAST STATE, TO DA JAKIES WYNIKI, ALE ZLE
}

class State(
    val origin_country: String,
    val time_position: Int,
    val longitude: Double,
    val latitude: Double,
    val velocity: Float,
    val true_track: Float
) {

    override fun toString(): String {
        return "State(time_position=$time_position, longitude=$longitude, latitude=$latitude, velocity=$velocity, true_track=$true_track)"
    }
}



