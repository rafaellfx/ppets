package br.com.rafaellfx.ppets.ui.aboutpet

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.AboutPetFragmentBinding
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.about_pet_fragment.*
import kotlinx.android.synthetic.main.new_pet_fragment.*
import java.io.ByteArrayOutputStream

class AboutPetFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var picture: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: AboutPetFragmentBinding


    companion object {
        fun newInstance(pet: Pet): AboutPetFragment {
            val f = AboutPetFragment()

            val args = Bundle()
            args.putSerializable("pet", pet)
            f.arguments = args

            return f
        }
    }

    private lateinit var viewModel: AboutPetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.about_pet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutPetViewModel::class.java)

        binding = DataBindingUtil.setContentView(activity!!,R.layout.about_pet_fragment)
        binding.viewModel = viewModel
        viewModel.pet = arguments?.getSerializable("pet") as Pet

        binding.ivEditprofile.setOnClickListener { takePicture() }
        binding.editPhoto.setOnClickListener { takePicture() }
        binding.btnEditSalvar.setOnClickListener { updatePicture() }

        if(viewModel.pet!!.photoUrl.isEmpty()){
            binding.editPhoto.visibility = View.GONE
            binding.ivEditprofile.visibility = View.VISIBLE
        }

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
//                binding.ivEditprofile.layoutParams.height = 500
//                binding.ivEditprofile.layoutParams.width = 500
//                binding.ivEditprofile.isOval = true
                binding.editPhoto.visibility = View.VISIBLE
                binding.ivEditprofile.visibility = View.GONE

                val bitmap = BitmapDrawable(resources, picture)
                binding.editPhoto.setImageDrawable(bitmap)

            }
        }
    }

    fun updatePicture(){

        if (editName.text.isNotEmpty() || editDescription.text.isNotEmpty()) {
            showProgress(true)
            var firebaseStorage = FirebaseStorage.getInstance()
            var referec = firebaseStorage.reference.child(viewModel.pet!!.namePhoto)

            val baos = ByteArrayOutputStream()
            picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val uploadTask = referec.putBytes(baos.toByteArray())

            uploadTask.addOnSuccessListener {
                uploadTask.continueWithTask { tasck ->
                    if (!tasck.isSuccessful) {
                        Log.d("teste", tasck.exception.toString())
                    }
                    referec.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val namePhoto = referec.name
                        update(downloadUri.toString(), namePhoto)
                    }
                }
            }
        }
    }


    fun update(uri: String, namePhoto: String){
        if (editName.text.isNotEmpty() || editDescription.text.isNotEmpty()) {

            var name = editName.text.toString()
            var description = editDescription.text.toString()
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    Log.d("teste", it.latitude.toString())

                    LocationService.save(Location("", it.latitude, it.longitude)).addOnSuccessListener{
                        var ids = ArrayList<String>()
                        ids.add(it.id)
                        PetsService.save(Pet(viewModel.pet!!.id,name,description,uri,namePhoto,ids)).addOnSuccessListener { showProgress(false) }
                    }
                }

        }
    }

    private fun showProgress(show: Boolean) {
        if(show){
            editProgress_circular.visibility = View.VISIBLE
            editName.visibility = View.GONE
            editDescription.visibility = View.GONE
            ivEditprofile.visibility = View.GONE
            btnEditSalvar.visibility = View.GONE
        }else{
            activity!!.finish()
        }
    }

}
