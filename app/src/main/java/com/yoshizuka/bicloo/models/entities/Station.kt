package com.yoshizuka.bicloo.models.entities

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

/**
 * Objet définissant ce qu'est une sttion
 */
data class Station(
        /**
         * Identifiant d'une station
         */
        @SerializedName("number")
        var id: Int,

        /**
         * Nom de la station
         */
        @SerializedName("name")
        var name: String,

        /**
         * Adresse de la station
         */
        @SerializedName("address")
        var address: String,

        /**
         * Localisation de la station
         */
        @SerializedName("position")
        var position: Position,

        /**
         * Si un terminal de paiement est disponible à la station
         */
        @SerializedName("banking")
        var banking: Boolean,

        /**
         * Station bonus ?
         */
        @SerializedName("bonus")
        var bonus: Boolean,

        /**
         * Status de la station (OPEN, CLOSED)
         */
        @SerializedName("status")
        var status: String,

        /**
         * Nombre de vélos possible à la station
         */
        @SerializedName("bike_stands")
        var  bikeStands: Int,

        /**
         * Nombre de places restante
         */
        @SerializedName("available_bike_stands")
        var availableBikeStands: Int,

        /**
         * Nombre de vélos encore disponible
         */
        @SerializedName("available_bikes")
        var availableBikes: Int,

        /**
         * Dernière mise à jour de la station
         */
        @SerializedName("last_update")
        var lastUpdate: Long) {

    /**
     * Convertit une position en LatLng pour google map
     */
    fun getMapPosition(): LatLng = LatLng(position.lat, position.lng)

    companion object {
        /**
         * Les status disponible des stations
         */
        const val STATUS_CLOSED: String = "CLOSED"
        const val STATUS_OPEN: String = "OPEN"
    }
}