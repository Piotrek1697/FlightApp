package com.example.flightapp.JsonFetch

class State(
    val callsign: String,
    val origin_country: String,
    val time_position: Int,
    val longitude: Double,
    val latitude: Double,
    val velocity: Float,
    val true_track: Float,
    val geo_altitude: Float
) {
    override fun toString(): String {
        return "State(callsign='$callsign', origin_country='$origin_country', time_position=$time_position, longitude=$longitude, latitude=$latitude, velocity=$velocity, true_track=$true_track, geo_altitude=$geo_altitude)"
    }
}