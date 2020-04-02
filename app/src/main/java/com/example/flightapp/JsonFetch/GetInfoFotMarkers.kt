package com.example.flightapp.JsonFetch


class GetInfoForMarkers(allFlights: AllFlights) {


    val allFlights = allFlights;

    fun getLongitude(position:Int):Double{
        return allFlights.states[position][5].toDouble()
    }

    fun getLatitude(position: Int):Double{
        return allFlights.states[position][6].toDouble()
    }

    fun getOriginCountry(position: Int):String{
        return allFlights.states[position][2]
    }

}