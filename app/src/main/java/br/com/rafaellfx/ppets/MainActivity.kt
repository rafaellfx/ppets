package br.com.rafaellfx.ppets

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = BottomNavigationView

        getGps()
        isLoggedIn()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listPetFragment,
                R.id.lostFragment
            )
        )

        NavigationUI.setupActionBarWithNavController(this, navController)

        NavigationUI.setupWithNavController(bottomNavigation, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(
                    R.id.nav_new_Pet_Fragment,
                    R.id.nav_about_petFragment,
                    R.id.editPetFragment
                )
            ) {
                bottomNavigation.visibility = View.GONE

            } else {
                bottomNavigation.visibility = View.VISIBLE
            }
        }

    }


    private fun getGps() {
        val service =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Verifica se o GPS está ativo
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // Caso não esteja ativo abre um novo diálogo com as configurações para
        // realizar se ativamento
        if (!enabled) {
            val builder =
                AlertDialog.Builder(this)
            builder.setTitle("Atenção")
            builder.setMessage("É necessário que sua localização esteja habilitada para o funcionamento do PPET, caso contrario será fechado.\n\nVocê deseja habilitar?")
            builder.setCancelable(true)
            builder.setPositiveButton("Sim") { dialogInterface: DialogInterface?, _: Int ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                dialogInterface?.dismiss()
                finish()
            }
            builder.setNegativeButton("Não")
            { dialogInterface: DialogInterface?, _: Int ->
                finish()
                dialogInterface?.dismiss()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pet_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    private fun isLoggedIn() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            finish()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

}
