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

}
