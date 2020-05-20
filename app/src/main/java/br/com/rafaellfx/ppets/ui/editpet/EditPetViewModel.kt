package br.com.rafaellfx.ppets.ui.editpet

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

class EditPetViewModel : ViewModel() {
    var pet: Pet? = null

    var isLoader = MutableLiveData(true)

    fun updatePicture(picture: Bitmap, fusedLocationClient: FusedLocationProviderClient, pet: Pet) {

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

    fun update(
        uri: String = "",
        namePhoto: String = "",
        fusedLocationClient: FusedLocationProviderClient,
        pet: Pet
    ) {

        if (uri.isEmpty() && namePhoto.isEmpty()) {

            PetsService.update(pet).addOnSuccessListener { isLoader.value = false }

        } else {

            pet.photoUrl = uri
            pet.namePhoto = namePhoto

            var latitude = 0.0
            var longitude = 0.0

            fusedLocationClient.lastLocation
                .addOnSuccessListener { it ->
                    latitude = it.latitude
                    longitude = it.latitude
                    LocationService.save(Location("", it.latitude, it.longitude))
                        .addOnSuccessListener { location ->

                            pet.locationId.add(location.id)
                            val geoFirestore = GeoFire(LocationService.service.firebase)
                            geoFirestore.setLocation(location.id, latitude, longitude)
                            PetsService.update(pet).addOnSuccessListener { isLoader.value = false }
                        }
                }
        }
    }


}
