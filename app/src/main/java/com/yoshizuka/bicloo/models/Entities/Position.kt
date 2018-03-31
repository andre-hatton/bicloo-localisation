package com.yoshizuka.bicloo.models.Entities

import com.google.gson.annotations.SerializedName

/**
 * Objet définissant la position d'une station (géographiquement)
 */
data class Position(
        /**
         * Latitude
         */
        @SerializedName("lat")
        var lat: Float,

        /**
         * Longitude
         */
        @SerializedName("lng")
        var lng: Float)