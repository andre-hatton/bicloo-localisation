package com.yoshizuka.bicloo.models.Entities

import com.google.gson.annotations.SerializedName

data class Position(
        @SerializedName("lat")
        var lat: Float,
        @SerializedName("lng")
        var lng: Float)