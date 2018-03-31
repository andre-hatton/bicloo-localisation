package com.yoshizuka.bicloo.models

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.yoshizuka.bicloo.models.Entities.Station

class StationModel(mContext: Context) : ApplicationModel(mContext) {


    fun getStation(stationId: Int) {
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations/${stationId}?contract=${DEFAULT_CONTRACT_NAME}&apiKey=${API_KEY}", {
            println(it)
        }, {
            println(it.message)
        }))
    }

    fun getStations() {
        getJson(StringRequest(Request.Method.GET, "${BASE_API_URL}stations?contract=${DEFAULT_CONTRACT_NAME}&apiKey=${API_KEY}", {
            println(it)
        }, {
            println(it.message)
        }))
    }

}