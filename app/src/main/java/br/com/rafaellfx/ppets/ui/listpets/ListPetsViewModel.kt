package br.com.rafaellfx.ppets.ui.listpets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.PetsService
import com.google.firebase.auth.FirebaseAuth

class ListPetsViewModel : ViewModel() {

    var listPets: MutableLiveData<List<Pet>> = MutableLiveData<List<Pet>>()

    fun getPets() {

        val pets: MutableList<Pet> = ArrayList<Pet>()

        PetsService.findAll().addOnSuccessListener  { p ->
            p.map  { pet ->

                var locationId: ArrayList<String> = ((if (pet.data["locationId"] != null) pet.data["locationId"]  else ArrayList<String>()) as ArrayList<String>)
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

            listPets.value = pets
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
