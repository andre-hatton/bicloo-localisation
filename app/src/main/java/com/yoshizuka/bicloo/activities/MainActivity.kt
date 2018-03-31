package com.yoshizuka.bicloo.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.databinding.ActivityMainBinding
import com.yoshizuka.bicloo.models.Entities.Position
import com.yoshizuka.bicloo.models.Entities.Station
import com.yoshizuka.bicloo.models.StationModel

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
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        updateMap()
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
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f))
            }
        } else {
            print("ACCES FAIL")
        }

        // gestion des marqueurs
        mStations.forEach {
            // ajout d'un marqueur sur les stations
            mMap?.addMarker(
                    MarkerOptions().position(LatLng(it.position.lat, it.position.lng)))

            // gestion du clic sur un marqueur
            mMap?.setOnMarkerClickListener { marker ->
                val station = mStations.find { it.position.equals(Position(marker.position.latitude, marker.position.longitude)) }
                station != null
            }
        }
    }
}
