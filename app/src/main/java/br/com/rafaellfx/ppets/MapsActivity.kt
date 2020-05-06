package br.com.rafaellfx.ppets

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.BufferedInputStream
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pet = intent.getSerializableExtra("pet") as Pet
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

        var locations = ArrayList<Location>()
        pet.locationId.map {document ->
            LocationService.findId(document).get().addOnSuccessListener { documentSnapshot ->
                locations.add(
                    Location(documentSnapshot.id,
                        documentSnapshot.data?.get("latitude") as Double,
                        documentSnapshot.data?.get("longitude") as Double)
                )
            }.continueWith {
                locations.map {location ->
                    val dot = LatLng(location.latidute, location.longitude)

                    mMap.addMarker(MarkerOptions().position(dot).title("${pet.name}"))

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dot,15f))
                }
            }
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
