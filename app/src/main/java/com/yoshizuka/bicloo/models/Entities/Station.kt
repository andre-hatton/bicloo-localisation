package com.yoshizuka.bicloo.models.Entities

import com.google.gson.annotations.SerializedName

data class Station(

        @SerializedName("number")
        var id: Int,

        @SerializedName("name")
        var name: String,

        @SerializedName("address")
        var address: String,

        @SerializedName("position")
        var position: Position,

        @SerializedName("banking")
        var banking: Boolean,

        @SerializedName("bonus")
        var bonus: Boolean,

        @SerializedName("status")
        var status: String,

        @SerializedName("bike_stands")
        var  bikeStands: Int,

        @SerializedName("available_bike_stands")
        var availableBikeStands: Int,

        @SerializedName("available_bikes")
        var availableBikes: Int,

        @SerializedName("last_update")
        var lastUpdate: Long)