package br.com.rafaellfx.ppets

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.ui.listpets.ListPetsFragment
import com.firebase.ui.auth.AuthUI

class ListPets : AppCompatActivity() {

    private var listPets: ArrayList<Pet> = ArrayList()
    private val PERMISSIONS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_pets_activity)

        this.getPermissions()

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

    private fun getPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSIONS)

        }else{
            Log.d("teste", "Permissão ok")
        }
    }


}
