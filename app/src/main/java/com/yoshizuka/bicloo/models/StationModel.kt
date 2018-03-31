package com.yoshizuka.bicloo.models

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.beust.klaxon.*
import com.google.gson.reflect.TypeToken
import com.yoshizuka.bicloo.models.entities.Station
import com.google.gson.Gson
import com.google.android.gms.maps.model.LatLng
import com.yoshizuka.bicloo.models.entities.Position


/**
 * Gestion des données des stations
 */
class StationModel(mContext: Context) : ApplicationModel(mContext) {

    var mStationModelListener: StationModelListener? = null
        set(value) {
            field = value
        }

    init {
        if(mContext is StationModelListener) {
            mStationModelListener = mContext
        }
    }

    /**
     * Permet de recuperer les données d'une station donnée
     * @param stationId L'identifiant de la station
     */
    fun getStation(stationId: Int) {
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations/${stationId}?contract=${DEFAULT_CONTRACT_NAME}&apiKey=${API_KEY}", {
            val station = mGson.fromJson(it, Station::class.java)
            mStationModelListener?.onGetStation(station)
        }, {
            println(it.message)
        }))
    }

    /**
     * Permet de récupérer l'ensemble des stations d'une ville
     */
    fun getStations() {
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations?contract=${DEFAULT_CONTRACT_NAME}&apiKey=${API_KEY}", {
            val listType = object : TypeToken<ArrayList<Station>>() {}.type
            val stations: List<Station> = Gson().fromJson(it, listType)
            mStationModelListener?.onGetStations(stations)
        }, {
            println(it.message)
        }))
    }

    /**
     * Permet de récupérer un itinéraire d'un point A à un point B
     * @param origin Point d'origine
     * @param dest Point d'arrivée
     */
    fun getDirections(origin: Position, dest: Position) {

        // Origin of route
        val str_origin = "origin=" + origin.lat + "," + origin.lng

        // Destination of route
        val str_dest = "destination=" + dest.lat + "," + dest.lng

        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=bicycling"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode"

        // Output format
        val output = "json"

        // Building the url to the web service


        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"

        getJson(StringRequest(Request.Method.GET, url, {
            Log.d("map", it)
            val parser: Parser = Parser()
            val stringBuilder: StringBuilder = StringBuilder(it)
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            val routes = json.array<JsonObject>("routes")

            val points: JsonArray<JsonObject> =
                    routes?.get("legs")?.get("steps")?.get(0) as? JsonArray<JsonObject>? ?: JsonArray()

            mStationModelListener?.onGetDestination(points)

            //val routes = json.arr
            Log.d("map", "json : $json")

        }, {
            Log.d("map", it.message)
        }))
    }

    /**
     * Interface permettant de renvoyer les données à l'activity
     */
    interface StationModelListener {

        /**
         * Retourne les données d'une station
         * @param station Une station
         */
        fun onGetStation(station: Station)

        /**
         * Retourne la liste des stations
         * @param stations La listes des stations
         */
        fun onGetStations(stations: List<Station>)

        /**
         * Retourne la liste des étapes à prendre pour aller d'un point à un autre
         * @param points La liste de points
         */
        fun onGetDestination(points: JsonArray<JsonObject>)
    }

}