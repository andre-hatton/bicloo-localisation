package com.yoshizuka.bicloo.utils.binding

import android.databinding.BindingAdapter
import android.widget.TextView
import com.yoshizuka.bicloo.models.entities.Station

@BindingAdapter("android:text")
fun setStampAvailable(view: TextView, station: Station) {
    view.text = station.availableBikes.toString().plus("/").plus(station.bikeStands)
}
