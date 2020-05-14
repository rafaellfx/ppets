package br.com.rafaellfx.ppets.ui.newpet

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.storage.FirebaseStorage
import uk.co.mgbramwell.geofire.android.GeoFire
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class NewPetViewModel : ViewModel() {

    var isLoader = MutableLiveData<Boolean>()

    fun savePet(
        name: String,
        description: String,
        fusedLocationClient: FusedLocationProviderClient,
        photoUrl: String = "",
        namePhoto: String = ""
    ) {
        isLoader.value = true
        fusedLocationClient.lastLocation.addOnSuccessListener {

            var location = Location("", it.latitude, it.longitude)
            LocationService.save(location)
                .addOnSuccessListener { document ->
                    var locations = ArrayList<String>()
                    locations.add(document.id)

                    isLoader.value = false
                    PetsService.save(Pet("", name, description, photoUrl, namePhoto, locations))
                    val geoFirestore = GeoFire(LocationService.service.firebase)
                    geoFirestore.setLocation(document.id, location.latidute, location.longitude)
                }
        }


    }

    fun savePicture(
        name: String,
        description: String,
        fusedLocationClient: FusedLocationProviderClient,
        picture: Bitmap
    ) {
        isLoader.value = true
        var fileName = "${Calendar.getInstance().timeInMillis}.jpg"
        var firebaseStorage = FirebaseStorage.getInstance()
        var referec = firebaseStorage.reference.child(fileName)

        val baos = ByteArrayOutputStream()
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uploadTask = referec.putBytes(baos.toByteArray())

        uploadTask.addOnFailureListener { exception ->
            Log.d("teste", exception.toString())
        }.addOnSuccessListener {
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.d("teste", task.exception.toString())
                }
                referec.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    savePet(
                        name,
                        description,
                        fusedLocationClient,
                        task.result.toString(),
                        referec.name
                    )
                }
            }
        }
    }

}
