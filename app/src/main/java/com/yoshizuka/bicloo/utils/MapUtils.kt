package com.yoshizuka.bicloo.utils

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.yoshizuka.bicloo.models.entities.Station

/**
 * Classe utilitaire pour la gestion de google map
 */
open class MapUtils {
    companion object {

        /**
         * Method to decode polyline points
         * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
         * @param encoded La polyline à decoder
         * @return Liste des points géographiques
         */
        fun decodePoly(encoded: String): List<LatLng> {
            val poly = ArrayList<LatLng>()
            var index = 0
            val len = encoded.length
            var lat = 0
            var lng = 0

            while (index < len) {
                var b: Int
                var shift = 0
                var result = 0
                do {
                    b = encoded[index++].toInt() - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dlat

                shift = 0
                result = 0
                do {
                    b = encoded[index++].toInt() - 63
                    result = result or (b and 0x1f shl shift)
                    shift += 5
                } while (b >= 0x20)
                val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dlng

                val p = LatLng(lat.toDouble() / 1E5,
                        lng.toDouble() / 1E5)
                poly.add(p)
            }

            Log.d("poly", encoded + " => " + poly)
            return poly
        }

        /**
         * Calcul la distance entre 2 points
         * @param origin Premier point
         * @param dest Second point
         * @return Distance entre les 2 points
         */
        fun getDistance(origin: LatLng, dest: LatLng): Double {
            val x = Math.abs(origin.latitude - dest.latitude)
            val y = Math.abs(origin.longitude - dest.longitude)
            return Math.sqrt(x * x + y * y)
        }

        fun getCloserStation(startPosition: LatLng, stations: List<Station>) : Station {
            var closerStation = stations[0]
            var minDistance = getDistance(startPosition, closerStation.getMapPosition())
            stations.forEach {
                val distance = getDistance(startPosition, it.getMapPosition())
                if(distance < minDistance) {
                    minDistance = distance
                    closerStation = it
                }

            }
            return closerStation
        }
    }
}