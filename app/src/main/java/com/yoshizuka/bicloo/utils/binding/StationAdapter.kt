package com.yoshizuka.bicloo.utils.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.models.entities.Station

/**
 * Affiche le nombre de vélos disponible
 * @param view Le TextView
 * @param station La station
 */
@BindingAdapter("android:bikes")
fun setBikeAvailable(view: TextView, station: Station?) {
    if(station != null) {
        view.text = view.context.resources.getQuantityString(R.plurals.bike_available_text, station.availableBikes, station.availableBikes)
    }
}

/**
 * Affiche le nombre de place disponible
 * @param view Le TextView
 * @param station La station
 */
@BindingAdapter("android:stands")
fun setStandAvailable(view: TextView, station: Station?) {
    if(station != null) {
        view.text = view.context.resources.getQuantityString(R.plurals.stand_available_text, station.availableBikeStands, station.availableBikeStands)
    }
}
