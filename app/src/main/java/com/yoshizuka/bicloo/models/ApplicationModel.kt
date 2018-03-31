package com.yoshizuka.bicloo.models

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

/**
 * Model parent permettant de généraliser les fonctionnalité des enfants
 */
abstract class ApplicationModel(val mContext: Context) {

    val mRequestQueue: RequestQueue = Volley.newRequestQueue(mContext)


    protected companion object {
        /**
         * Clé de l'api jcdecaux
         */
        val API_KEY: String = "f77445070233c5423ac1d90c604ffa9c860d0416"

        /**
         * L'url de base de l'api
         */
        val BASE_API_URL: String = "https://api.jcdecaux.com/vls/v1/"

        /**
         * Le contrat utilisé par défaut (pour une futur amélioration)
         */
        val DEFAULT_CONTRACT_NAME: String = "Nantes"
    }

    fun getJson(request: StringRequest) {
        mRequestQueue.add(request)
    }
}