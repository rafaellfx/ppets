package br.com.rafaellfx.ppets.ui.newpet

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.NewPetFragmentBinding
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.PetsService
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.list_pets_fragment.*
import kotlinx.android.synthetic.main.new_pet_fragment.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class NewPetFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var picture: Bitmap

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
                //binding.ivProfile.setImageBitmap(picture)
                iv_profile.visibility = View.GONE
                tvInform.visibility = View.GONE
                val bitmap = BitmapDrawable(resources, picture)
                relativeLayout.setBackground(bitmap)

            }
        }
    }

    private fun save(photo: String = "") {

        if (edName.text.isNotEmpty() || edDescription.text.isNotEmpty()) {
            PetsService.save(
                Pet(
                    "",
                    edName.text.toString(),
                    edDescription.text.toString(),
                    photo,
                    "logationId"
                )
            )
            activity!!.finish()
        }
    }

    private fun addPicture() {
        if (this::picture.isInitialized) {
            var fileName = "${Calendar.getInstance().timeInMillis}.jpg"
            var firebaseStorage = FirebaseStorage.getInstance()
            var referec = firebaseStorage.reference.child(fileName)
            var uriForReturn: String = ""

            val baos = ByteArrayOutputStream()
            picture.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            val uploadTask = referec.putBytes(baos.toByteArray())

            uploadTask.addOnFailureListener { exception ->
                Log.d("teste", exception.toString())
            }.addOnSuccessListener {
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        Log.d("teste", task.exception.toString())
                    }
                    referec.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        save(downloadUri.toString())
                    }
                }
            }
        } else {
            save()
        }
    }
}
