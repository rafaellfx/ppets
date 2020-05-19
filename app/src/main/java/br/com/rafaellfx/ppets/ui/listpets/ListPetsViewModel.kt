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


    private val QUERY_RADIUS = 5.0

    var listPets: MutableLiveData<List<Pet>> = MutableLiveData<List<Pet>>()
    var isDownload: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationsIdsNoRecused = ArrayList<String>()

    fun loadPets(context: Context) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Pega localizacao do e add em arrayList
        fusedLocationClient.lastLocation.addOnSuccessListener {location ->

            if (location != null) {

                val geoFirestore = GeoFire(LocationService.service.firebase)

                val queryLocation =
                    QueryLocation.fromDegrees(location.latitude, location.longitude)

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
                        } else {
                            Log.e("LOG_PPET", task.exception.toString())
                        }
                    }.addOnSuccessListener {
                        var pets = ArrayList<Pet>()
                        PetsService.findAll()
                            .addOnSuccessListener { p ->


                                p.map { pet ->

                                    var locationId: ArrayList<String> =
                                        ((if (pet.data["locationId"] != null) pet.data["locationId"] else ArrayList<String>()) as ArrayList<String>)

                                     Log.e("LOG_PPET", "${locationsIdsNoRecused.contains(locationId.last())}")
                                     Log.e("LOG_PPET", " location ${locationsIdsNoRecused}")
                                     Log.e("LOG_PPET", " pesquisa ${locationId.last()}")

                                    locationId.map {place ->

                                        if (locationsIdsNoRecused.contains(place)) {

                                            pets.add(
                                                Pet(
                                                    pet.id,
                                                    pet.data["name"].toString(),
                                                    pet.data["description"].toString(),
                                                    pet.data["photoUrl"].toString(),
                                                    pet.data["namePhoto"].toString(),
                                                    pet.data["lost"] as Boolean,
                                                    locationId
                                                )
                                            )
                                        }
                                    }
                                }
                                isDownload.value = false
                                listPets.value = pets
                            }
                    }
            }
        }.addOnFailureListener { ex ->
            Log.e("LOG_PPET", ex.toString())
        }

    }

}
