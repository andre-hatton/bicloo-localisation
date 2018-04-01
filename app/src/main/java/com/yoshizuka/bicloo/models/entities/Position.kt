package com.yoshizuka.bicloo.models.entities

import android.os.Parcel
import android.os.Parcelable
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
        var lng: Double) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readDouble(),
                parcel.readDouble())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeDouble(lat)
                parcel.writeDouble(lng)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Position> {
                override fun createFromParcel(parcel: Parcel): Position {
                        return Position(parcel)
                }

                override fun newArray(size: Int): Array<Position?> {
                        return arrayOfNulls(size)
                }
        }
}