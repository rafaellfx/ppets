package br.com.rafaellfx.ppets.ui.listpets

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import uk.co.mgbramwell.geofire.android.GeoFire
import uk.co.mgbramwell.geofire.android.model.Distance
import uk.co.mgbramwell.geofire.android.model.DistanceUnit
import uk.co.mgbramwell.geofire.android.model.QueryLocation


class ListPetsViewModel : ViewModel() {

    val TAG = "LOG_PPET"
    private val QUERY_CENTER = GeoPoint(-30.036036036036037, -51.11667124798394)
    private val QUERY_RADIUS = 5.0

    var listPets: MutableLiveData<List<Pet>> = MutableLiveData<List<Pet>>()
    var isDownload: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationsIdsNoRecused = ArrayList<String>()

    fun loadPets(context: Context) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val pets: MutableList<Pet> = ArrayList<Pet>()

        // Pega localizacao do device e add em arrayList
        fusedLocationClient.lastLocation.addOnSuccessListener {
            val geoFirestore = GeoFire(LocationService.service.firebase)

            val queryLocation =
                QueryLocation.fromDegrees(it.latitude, it.longitude)

            val searchDistance = Distance(5.0, DistanceUnit.KILOMETERS)
            geoFirestore.query()
                // .whereEqualTo("title", "The Title")
                .whereNearTo(queryLocation, searchDistance)
                .build()
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.addOnSuccessListener { locationsIds ->
                            locationsIdsNoRecused.add(locationsIds.documents.last().id)
                        }
                    }
                }
        }.addOnSuccessListener {

            PetsService.findAll()
                .addOnSuccessListener { p ->
                    p.map { pet ->

                        var locationId: ArrayList<String> =
                            ((if (pet.data["locationId"] != null) pet.data["locationId"] else ArrayList<String>()) as ArrayList<String>)
                        if (locationsIdsNoRecused.contains(locationId.last())) {
                            pets.add(
                                Pet(
                                    pet.id,
                                    pet.data["name"].toString(),
                                    pet.data["description"].toString(),
                                    pet.data["photoUrl"].toString(),
                                    pet.data["namePhoto"].toString(),
                                    locationId
                                )
                            )
                        }
                    }
                    isDownload.value = false
                    listPets.value = pets
                }
        }


    }

    fun isLoggedIn(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            return false
        }
        return true
    }


}
