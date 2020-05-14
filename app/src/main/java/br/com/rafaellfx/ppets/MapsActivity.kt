package br.com.rafaellfx.ppets

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.navArgs
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.BufferedInputStream
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val safeArgs: MapsActivityArgs by navArgs()
        pet = safeArgs.pet

        setTitle("Localidades de ${pet.name}")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { myLocation ->
            mMap.addCircle(
                CircleOptions()
                    //.center(LatLng(-30.036036036036037, -51.11667124798394))
                    .center(LatLng(myLocation.latitude, myLocation.longitude))
                    .radius(5000.0)
                    .strokeColor(Color.argb(58, 93, 93, 139))
                    .fillColor(Color.argb(35, 83, 83, 199))
            )
        }
            var locations = ArrayList<Location>()
            pet.locationId.map { document ->
                LocationService.findId(document).get().addOnSuccessListener { documentSnapshot ->
                    locations.add(
                        Location(
                            documentSnapshot.id,
                            documentSnapshot.data?.get("latitude") as Double,
                            documentSnapshot.data?.get("longitude") as Double
                        )
                    )
                }.continueWith {
                    locations.map { location ->
                        val dot = LatLng(location.latidute, location.longitude)

                        mMap.addMarker(MarkerOptions().position(dot).title("${pet.name}"))

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dot, 13f))
                    }
                }
            }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
