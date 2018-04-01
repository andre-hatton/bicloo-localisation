package com.yoshizuka.bicloo.models.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
        var lastUpdate: Long) : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Position::class.java.classLoader),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong())

    /**
     * Convertit une position en LatLng pour google map
     */
    fun getMapPosition(): LatLng = LatLng(position.lat, position.lng)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeParcelable(position, flags)
        parcel.writeByte(if (banking) 1 else 0)
        parcel.writeByte(if (bonus) 1 else 0)
        parcel.writeString(status)
        parcel.writeInt(bikeStands)
        parcel.writeInt(availableBikeStands)
        parcel.writeInt(availableBikes)
        parcel.writeLong(lastUpdate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Station> {
        override fun createFromParcel(parcel: Parcel): Station {
            return Station(parcel)
        }

        override fun newArray(size: Int): Array<Station?> {
            return arrayOfNulls(size)
        }

        /**
         * Les status disponible des stations
         */
        const val STATUS_CLOSED: String = "CLOSED"
        const val STATUS_OPEN: String = "OPEN"
    }
}