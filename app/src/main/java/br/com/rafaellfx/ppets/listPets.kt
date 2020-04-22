package br.com.rafaellfx.ppets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.PetsService
import br.com.rafaellfx.ppets.ui.listpets.ListPetsFragment

class listPets : AppCompatActivity() {

    private var listPets: ArrayList<Pet> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_pets_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerActivity, ListPetsFragment.newInstance())
                .commitNow()
        }
    }

}
