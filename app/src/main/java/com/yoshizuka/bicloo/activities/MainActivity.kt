package com.yoshizuka.bicloo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.models.StationModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StationModel(this).getStations()
    }
}
