package com.yoshizuka.bicloo.models.entities

import com.google.gson.annotations.SerializedName

/**
 * Objet définissant la position d'une station (géographiquement)
 */
data class Position(
        /**
         * Latitude
         */
        @SerializedName("lat")
        var lat: Double,

        /**
         * Longitude
         */
        @SerializedName("lng")
        var lng: Double)