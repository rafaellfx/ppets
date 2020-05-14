package br.com.rafaellfx.ppets.ui.lost

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import uk.co.mgbramwell.geofire.android.GeoFire
import uk.co.mgbramwell.geofire.android.model.Distance
import uk.co.mgbramwell.geofire.android.model.DistanceUnit
import uk.co.mgbramwell.geofire.android.model.QueryLocation

class LostViewModel : ViewModel() {

    var isDownload = MutableLiveData<Boolean>(true)
    var listPetsLost: MutableLiveData<List<Pet>> = MutableLiveData<List<Pet>>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationsIdsNoRecused = ArrayList<String>()
    private val QUERY_RADIUS = 5.0

    fun loadPetLost(context: Context) {

        selfLocation(context)
            .addOnSuccessListener {

                var pets = ArrayList<Pet>()
                PetsService.find()
                    .addOnSuccessListener { losts ->
                        losts.map { lost ->
                            var locationId: ArrayList<String> =
                                ((if (lost.data["locationId"] != null) lost.data["locationId"] else ArrayList<String>()) as ArrayList<String>)

                            pets.add(
                                Pet(
                                    lost.id,
                                    lost.data["name"].toString(),
                                    lost.data["description"].toString(),
                                    lost.data["photoUrl"].toString(),
                                    lost.data["namePhoto"].toString(),
                                    lost.data["lost"] as Boolean,
                                    locationId
                                )
                            )
                        }
                        isDownload.value = false
                        listPetsLost.value = pets
                    }
            }
    }

    fun selfLocation(context: Context): Task<Location> {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val pets: MutableList<Pet> = ArrayList<Pet>()

        // Pega localizacao do device e add em arrayList
        return fusedLocationClient.lastLocation.addOnSuccessListener {
            val geoFirestore = GeoFire(LocationService.service.firebase)

            val queryLocation =
                QueryLocation.fromDegrees(it.latitude, it.longitude)

            val searchDistance = Distance(QUERY_RADIUS, DistanceUnit.KILOMETERS)
            geoFirestore.query()
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
        }
    }
}
