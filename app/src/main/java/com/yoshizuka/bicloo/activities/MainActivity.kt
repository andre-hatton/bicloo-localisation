package com.yoshizuka.bicloo.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.databinding.ActivityMainBinding
import com.yoshizuka.bicloo.fragments.StationBottomSheetFragment
import com.yoshizuka.bicloo.models.entities.Position
import com.yoshizuka.bicloo.models.entities.Station
import com.yoshizuka.bicloo.models.StationModel
import com.yoshizuka.bicloo.utils.MapUtils

/**
 * @author André Hatton
 * @date 29/03/2018
 * Activité principal de l'application
 */
class MainActivity : AppCompatActivity(), StationModel.StationModelListener, OnMapReadyCallback {

    /**
     * Vue de l'activité
     */
    lateinit var mBinding: ActivityMainBinding

    /**
     * Objet google map
     */
    var mMap: GoogleMap? = null

    /**
     * Le model de base de l'activité
     */
    lateinit var mModel: StationModel

    /**
     * La liste des stations
     */
    var mStations: List<Station> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // vérification des permissions necessaire
        checkPermission()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // chargement de google map
        val mMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this)

        mModel = StationModel(this)
        mModel.getStations()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Activity.RESULT_OK) {
            if (permissions.size == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateMap()
            }
        } else {
            checkPermission()
        }
    }

    override fun onGetStation(station: Station) {
        println(station)
    }

    override fun onGetStations(stations: List<Station>) {
        mStations = stations
        if(mMap != null) {
            updateMap()
        }
        mStations.forEachIndexed { index, it ->
            Log.d("Station","val station$index = Station(${it.id}, \"${it.name}\", \"\", Position(${it.position.lat}, ${it.position.lng}), ${it.banking}, ${it.bonus}, ${it.status}, ${it.bikeStands}, ${it.availableBikeStands}, ${it.availableBikes}, ${it.lastUpdate})")
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        updateMap()
    }


    override fun onGetDestination(points: JsonArray<JsonObject>) {
        val polypts = points.flatMap { MapUtils.decodePoly(it.obj("polyline")?.string("points")!!)  }
        val options = PolylineOptions()
        options.color(Color.BLUE)
        //options.width(13f)
        for (point in polypts) options.add(point)
        mMap?.addPolyline(options)
    }

    /**
     * Vérifie si des persmissions sont necessaire pour utiliser l'application
     */
    private fun checkPermission() {
        val permissions = ArrayList<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                updateMap()
            }
            val permission = permissions.toTypedArray()


            if (permission.isNotEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permission,
                        3)
            }
        }
    }

    /**
     * Mise à jour de la carte
     */
    private fun updateMap() {
        if (PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
            mMap?.isMyLocationEnabled = true
            mMap?.getUiSettings()?.isMyLocationButtonEnabled = true

            // permet de position la caméra sur la position de l'utilisateur à une distance respectable
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                Log.d("Station","val myPosition = LatLng(${it.latitude}, ${it.longitude})")
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f))
            }
        } else {
            print("ACCES FAIL")
        }

        if(mStations.size > 13)
            mModel.getDirections(mStations.get(2).position, mStations.get(13).position);

        // gestion des marqueurs
        mStations.forEach {
            // ajout d'un marqueur sur les stations
            val markerOptions = MarkerOptions().position(LatLng(it.position.lat, it.position.lng))
            if(it.status == Station.STATUS_CLOSED) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            } else if(it.availableBikes == 0) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            } else if(it.availableBikeStands == 0) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            } else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            }
            mMap?.addMarker(markerOptions)

            // gestion du clic sur un marqueur
            mMap?.setOnMarkerClickListener { marker ->
                val station = mStations.find { it.position.equals(Position(marker.position.latitude, marker.position.longitude)) }
                val stationFragment = StationBottomSheetFragment()
                stationFragment.mStation = station
                stationFragment.show(supportFragmentManager, "station")
                station != null
            }
        }
    }
}
