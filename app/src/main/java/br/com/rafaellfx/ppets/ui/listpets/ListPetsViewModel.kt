package br.com.rafaellfx.ppets.ui.listpets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.extensions.TAG
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
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

    fun loadPets(fusedLocationClient: FusedLocationProviderClient) {

        // Pega localizacao do e add em arrayList
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            Log.e(TAG, "$location")
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
                                locationsIds.documents.map { l ->

                                    locationsIdsNoRecused.add(l.id)
                                }
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

                                    locationId.map { place ->

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
                                            locationsIdsNoRecused.remove(place)
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
