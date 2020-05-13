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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import br.com.rafaellfx.ppets.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.new_pet_fragment.*

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        iv_profile.setOnClickListener { takePicture() }
        btnSalvar.setOnClickListener { addPicture() }

    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
            pictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {

                picture = data!!.extras!!.get("data") as Bitmap

                iv_profile.visibility = View.GONE
                tvInform.visibility = View.GONE
                val bitmap = BitmapDrawable(resources, picture)
                imgPetPhoto.visibility = View.VISIBLE
                imgPetPhoto.setImageDrawable(bitmap)


            }
        }
    }

    private fun addPicture() {
        showProgress()
        if (edName.text.isNotEmpty() || edDescription.text.isNotEmpty()) {


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

        viewModel.isLoader.observe(viewLifecycleOwner, Observer {
            if (it) {
                progress_circular.visibility = View.VISIBLE
                edName.visibility = View.GONE
                edDescription.visibility = View.GONE
                iv_profile.visibility = View.GONE
                btnSalvar.visibility = View.GONE
                imgPetPhoto.visibility = View.GONE
            } else {

                findNavController().popBackStack(R.id.navigation_home, false)
            }
        })

    }
}
