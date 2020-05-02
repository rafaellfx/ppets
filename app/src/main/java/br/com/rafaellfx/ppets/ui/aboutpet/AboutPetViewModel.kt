package br.com.rafaellfx.ppets.ui.aboutpet

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AboutPetViewModel : ViewModel() {
    var pet: Pet? = null

    var isLoader = MutableLiveData(true)


    fun updatePicture(picture: Bitmap,fusedLocationClient: FusedLocationProviderClient, pet:Pet){

        var firebaseStorage = FirebaseStorage.getInstance()
        var referec = firebaseStorage.reference.child(pet.namePhoto)

        val baos = ByteArrayOutputStream()
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uploadTask = referec.putBytes(baos.toByteArray())

        uploadTask.addOnSuccessListener {
            uploadTask.continueWithTask { tasck ->
                if (!tasck.isSuccessful) {
                    Log.d("teste", tasck.exception.toString())
                }
                referec.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    update(task.result.toString(), referec.name, fusedLocationClient, pet)
                }
            }
        }
    }

    fun update(uri: String ="", namePhoto: String="", fusedLocationClient: FusedLocationProviderClient, pet: Pet) {

        if (uri.isNotEmpty()) pet.photoUrl = uri
        if (namePhoto.isNotEmpty()) pet.namePhoto = namePhoto

        fusedLocationClient.lastLocation
            .addOnSuccessListener { it ->

                LocationService.save(Location("", it.latitude, it.longitude))
                    .addOnSuccessListener {

                        pet.locationId.add(it.id)

                        PetsService.update(pet).addOnSuccessListener { isLoader.value = false }
                    }
            }
    }
}
