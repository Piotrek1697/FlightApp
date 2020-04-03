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
                    list = parseToStatesList(allFlights)
                    countDownLatch.countDown()
                }
            })
            countDownLatch.await()
            return list
        }

        private fun parseToStatesList(flights: AllFlights): MutableList<State> {
            val list = mutableListOf<State>()
            flights.states.forEach { list.add(getState(it)) }
            return list
        }

        private fun getState(state: Array<String?>): State {
            val floatIndexes = listOf<Int>(5, 6, 9, 10, 13)
            if (state[3] == "" || state[3] == "null" || state[3] == null)
                state[3] = "0"
            floatIndexes.forEach {
                if (state[it] == "" || state[it] == "null" || state[it] == null)
                    state[it] = "0.0"
            }
            if (state[1] == "" || state[1] == "null" || state[1] == null)
                state[1] = "-"
            return State(
                state[1]!!.trim(),
                state[2]!!,
                state[3]!!.toInt(),
                state[5]!!.toDouble(),
                state[6]!!.toDouble(),
                state[9]!!.toFloat(),
                state[10]!!.toFloat(),
                state[13]!!.toFloat()
            )
        }
    }


}


class AllFlights(val states: MutableList<Array<String?>>) {
}




