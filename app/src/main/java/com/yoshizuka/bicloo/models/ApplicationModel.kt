package com.yoshizuka.bicloo.models

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.util.Log


/**
 * Model parent permettant de généraliser les fonctionnalité des enfants
 */
abstract class ApplicationModel(val mContext: Context) {

    /**
     * Initialisation des requête avec Volley
     */
    var mRequestQueue: RequestQueue? = null

    /**
     * Initialisation de gson
     */
    val mGson: Gson = GsonBuilder().create()

    init {
        if(isOnline()) {
            mRequestQueue = Volley.newRequestQueue(mContext)
        }
    }


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

    /**
     * Lance une requête à l'api
     * @param request La requête à envoyer
     */
    fun getJson(request: StringRequest) {
        Log.d("Request", request.url)
        mRequestQueue?.add(request)
    }

    /**
     * Vérifie que l'utilisateur est connecté à un réseau internet
     * @return true si la connexion existe
     */
    fun isOnline(): Boolean {
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}