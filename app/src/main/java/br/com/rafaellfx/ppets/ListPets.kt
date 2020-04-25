package br.com.rafaellfx.ppets

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.ui.listpets.ListPetsFragment
import com.firebase.ui.auth.AuthUI

class ListPets : AppCompatActivity() {

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.pet_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.sair -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                       finish()
                        startActivity(Intent(this, SignInActivity::class.java))
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
