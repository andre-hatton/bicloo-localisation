package com.yoshizuka.bicloo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.models.Entities.Station
import com.yoshizuka.bicloo.models.StationModel

/**
 * @author André Hatton
 * @date 29/03/2018
 * Activité principal de l'application
 */
class MainActivity : AppCompatActivity(), StationModel.StationModelListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StationModel(this).getStations()
    }

    override fun onGetStation(station: Station) {
        println(station)
    }

    override fun onGetStations(stations: List<Station>) {
        stations.forEach {
            println(it)
        }
    }
}
