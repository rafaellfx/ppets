package br.com.rafaellfx.ppets.ui.listpets

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.Pet

class ListPetsViewModel : ViewModel() {

    var listPets: MutableLiveData<List<Pet>> = MutableLiveData<List<Pet>>()

    fun getPets(){
        val dog = Pet("","Pluto", ArrayList<String>(),"","")
        val cat = Pet("","Frajola", ArrayList<String>(),"","")

        val pets: MutableList<Pet> = ArrayList<Pet>()
        pets.add(dog)
        pets.add(cat)

        listPets.value = pets
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("teste", "onCleared")
    }
}
