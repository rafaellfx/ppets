package br.com.rafaellfx.ppets.ui.newpet

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.storage.FirebaseStorage
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
                .addOnSuccessListener { it ->
                    var locations = ArrayList<String>()
                    locations.add(it.id)
                    PetsService.save(Pet("", name, description, photoUrl, namePhoto, locations))
                    isLoader.value = false
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
