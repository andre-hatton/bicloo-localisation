package com.yoshizuka.bicloo.models

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.reflect.TypeToken
import com.yoshizuka.bicloo.models.entities.Station
import com.google.gson.Gson



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
    }

}