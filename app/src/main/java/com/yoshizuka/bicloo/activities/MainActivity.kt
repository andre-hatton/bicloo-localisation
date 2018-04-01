package com.yoshizuka.bicloo.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.yoshizuka.bicloo.R
import com.yoshizuka.bicloo.databinding.ActivityMainBinding
import com.yoshizuka.bicloo.fragments.StationBottomSheetFragment
import com.yoshizuka.bicloo.fragments.StationFragment
import com.yoshizuka.bicloo.models.entities.Position
import com.yoshizuka.bicloo.models.entities.Station
import com.yoshizuka.bicloo.models.StationModel
import com.yoshizuka.bicloo.utils.MapUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author André Hatton
 * @date 29/03/2018
 * Activité principal de l'application
 */
class MainActivity : AppCompatActivity(), StationModel.StationModelListener, OnMapReadyCallback, StationFragment.OnStationFragmentListener {

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

    /**
     * Point de départ
     */
    var mStartPlace: Place? = null

    /**
     * Point d'arrivé
     */
    var mEndPlace: Place? = null

    /**
     * Fragment actuellement sur la vue
     */
    var mCurrentFragment: Fragment? = null

    /**
     * Le fragment de la carte
     */
    lateinit var mMapFragment: SupportMapFragment

    /**
     * Variables et fonctions static
     */
    companion object {
        private const val PLACE_PICKER_REQUEST = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // vérification des permissions necessaire
        checkPermission()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.activity = this


        setSupportActionBar(toolbar)
        toolbar?.navigationIcon = ContextCompat.getDrawable(this, R.mipmap.ic_drawer)
        toolbar?.setNavigationOnClickListener { drawer_layout?.openDrawer(left_drawer) }
        mMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().add(R.id.content, mMapFragment).commit()

        // chargement de google map
        mMapFragment.getMapAsync(this)

        mModel = StationModel(this)
        mModel.getStations()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                var addressText = place.name.toString()
                addressText += "\n" + place.address.toString()

                mMap?.addMarker(MarkerOptions().position(place.latLng))
                loadPlacePicker()
            }
        }
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

    override fun onGetDestination(points: JsonArray<JsonObject>) {
        val polypts = points.flatMap { MapUtils.decodePoly(it.obj("polyline")?.string("points")!!)  }
        val options = PolylineOptions()
        options.color(Color.BLUE)
        //options.width(13f)
        for (point in polypts) options.add(point)
        mMap?.addPolyline(options)
    }

    override fun onStationClick(station: Station?) {
        showStation(station)
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
                if(it != null) {
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f))
                } else {
                    mMap?.moveCamera(CameraUpdateFactory.zoomBy(14f))
                }
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
                showStation(station)
                station != null
            }
        }
    }

    /**
     * Affichage d'un bottom sheet pour les détails de la station
     * @param station La station à détailler
     */
    fun showStation(station: Station?) {
        if(station != null) {
            val stationFragment = StationBottomSheetFragment()
            stationFragment.mStation = station
            stationFragment.show(supportFragmentManager, "station")
        }
    }

    /**
     * Action permettant de faire une recherche
     */
    fun loadPlacePicker() {
        val builder = PlacePicker.IntentBuilder()

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    fun switchFragment() {
        if(mCurrentFragment is StationFragment) {
            mCurrentFragment = mMapFragment
            supportFragmentManager.popBackStack()
        } else {
            val stationFragment = StationFragment()
            stationFragment.items = mStations
            supportFragmentManager.beginTransaction().add(R.id.content, stationFragment).addToBackStack("stationFragment").commit()
            mCurrentFragment = stationFragment
        }
    }
}
