package br.com.rafaellfx.ppets.ui.newpet

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.new_pet_fragment.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class NewPetFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var picture: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        fun newInstance() = NewPetFragment()
    }

    private lateinit var viewModel: NewPetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.new_pet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewPetViewModel::class.java)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        iv_profile.setOnClickListener { takePicture() }
        btnSalvar.setOnClickListener { addPicture() }

    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {

                picture = data!!.extras!!.get("data") as Bitmap
                iv_profile.layoutParams.height = 500
                iv_profile.layoutParams.width = 500
                iv_profile.isOval = false

                tvInform.visibility = View.GONE
                val bitmap = BitmapDrawable(resources, picture)
                iv_profile.setImageDrawable(bitmap)

            }
        }
    }

    private fun addPicture() {
        if (edName.text.isNotEmpty() || edDescription.text.isNotEmpty()) {
            showProgress()

            var name = edName.text.toString()
            var description = edDescription.text.toString()

            if (this::picture.isInitialized) {

                viewModel.savePicture(name,description,fusedLocationClient,picture)
            } else {
                viewModel.savePet(name, description,fusedLocationClient)
            }


        }

    }

    private fun showProgress() {

        viewModel.isLoader.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                progress_circular.visibility = View.VISIBLE
                edName.visibility = View.GONE
                edDescription.visibility = View.GONE
                iv_profile.visibility = View.GONE
                btnSalvar.visibility = View.GONE
                tvInform.visibility = View.GONE
            } else {
                activity!!.finish()
            }
        })

    }
}
