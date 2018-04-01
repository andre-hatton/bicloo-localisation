package com.yoshizuka.bicloo.models

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.beust.klaxon.*
import com.google.gson.reflect.TypeToken
import com.yoshizuka.bicloo.models.entities.Station
import com.google.gson.Gson
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.models.entities.Position


/**
 * Gestion des données des stations
 */
class StationModel(mContext: Context) : ApplicationModel(mContext) {

    private var mStationModelListener: StationModelListener? = null

    init {
        if(mContext is StationModelListener) {
            mStationModelListener = mContext
        }
    }

    @Suppress("unused")
            /**
     * Permet de recuperer les données d'une station donnée
     * @param stationId L'identifiant de la station
     */
    fun getStation(stationId: Int) {
        if(offline()) return
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations/$stationId?contract=$DEFAULT_CONTRACT_NAME&apiKey=$API_KEY", {
            val station = mGson.fromJson(it, Station::class.java)
            mStationModelListener?.onGetStation(station)
        }, {
            mStationModelListener?.onError(it.message ?: "")
        }))
    }

    /**
     * Permet de récupérer l'ensemble des stations d'une ville
     */
    fun getStations() {
        if(offline()) return
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations?contract=$DEFAULT_CONTRACT_NAME&apiKey=$API_KEY", {
            val listType = object : TypeToken<ArrayList<Station>>() {}.type
            val stations: List<Station> = Gson().fromJson(it, listType)
            mStationModelListener?.onGetStations(stations)
        }, {
            mStationModelListener?.onError(it.message ?: "")
        }))
    }

    /**
     * Permet de récupérer un itinéraire d'un point A à un point B
     * @param origin Point d'origine
     * @param dest Point d'arrivée
     */
    fun getDirections(origin: Position, dest: Position) {
        Log.d("model", "get direction")
        if(offline()) return

        // Origin of route
        val strOrigin = "origin=" + origin.lat + "," + origin.lng

        // Destination of route
        val strDest = "destination=" + dest.lat + "," + dest.lng

        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=bicycling"

        // Building the parameters to the web service
        val parameters = "$strOrigin&$strDest&$sensor&$mode"

        // Output format
        val output = "json"

        // Building the url to the web service


        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"

        getJson(StringRequest(Request.Method.GET, url, {
            Log.d("map", it)
            val parser = Parser()
            val stringBuilder = StringBuilder(it)
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            val routes = json.array<JsonObject>("routes")

            @Suppress("UNCHECKED_CAST")
            val points: JsonArray<JsonObject> =
                    routes?.get("legs")?.get("steps")?.getOrNull(0) as? JsonArray<JsonObject>? ?: JsonArray()

            mStationModelListener?.onGetDestination(points)

        }, {
            mStationModelListener?.onError(it.message ?: "")
        }))
    }

    /**
     * Vérifie et envoie un message si l'utilisateur est offline
     */
    private fun offline(): Boolean {
        val isOnline = isOnline()
        if(!isOnline) {
            mStationModelListener?.onError(mContext.getString(R.string.offline_error_msg))
        }
        return !isOnline
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

        /**
         * Si une erreur est retournée
         * @param message optionnel
         */
        fun onError(message: String = "")
    }

}