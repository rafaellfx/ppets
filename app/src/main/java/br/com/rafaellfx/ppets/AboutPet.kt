package br.com.rafaellfx.ppets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.ui.aboutpet.AboutPetFragment

class AboutPet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_pet_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pet = intent.getSerializableExtra("pet") as Pet

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AboutPetFragment.newInstance(pet))
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
