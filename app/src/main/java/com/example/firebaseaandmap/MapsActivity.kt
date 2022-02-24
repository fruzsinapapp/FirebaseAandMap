package com.example.firebaseaandmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.firebaseaandmap.databinding.ActivityMapsBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {//with this we can interact with the map {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment //id which we give in the activity_maps.xml
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val myVillage = LatLng(47.67555689479374, 18.680194743899435)
        val zoomLevel = 15f //the higher this value the more zoom we get
        mMap.addMarker(MarkerOptions().position(myVillage).title("This is Sárisáp"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myVillage,zoomLevel.toFloat()))

        setMapLongClick(mMap)
        setPoiClick(mMap)
        //places of interest
    }


    private fun setMapLongClick(map:GoogleMap){
        map.setOnMapLongClickListener {latlng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.2f, Lng: %2$.2f", //2 decimal points
                latlng.latitude,
                latlng.longitude
            )

            map.addMarker(
                MarkerOptions().position(latlng).title("Marker here").snippet(snippet)
            )

            val database = Firebase.database("https://fir-aandmap-default-rtdb.firebaseio.com/")
            val reference = database.reference
            val data = reference.push().child("location").setValue(latlng)


        }
    }

    private fun setPoiClick(map:GoogleMap){
        map.setOnPoiClickListener{point ->
            val poiMarker = map.addMarker(MarkerOptions().position(point.latLng).title(point.name))
            poiMarker?.showInfoWindow()
        }
    }
}