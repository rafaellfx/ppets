package br.com.rafaellfx.ppets.ui.editpet

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.rafaellfx.ppets.MainActivity
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.EditPetFragmentBinding
import br.com.rafaellfx.ppets.model.Pet
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.edit_pet_fragment.*
import java.util.*

class EditPetFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var picture: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: EditPetFragmentBinding

    companion object {
        fun newInstance(pet: Pet): EditPetFragment {
            val f = EditPetFragment()

            val args = Bundle()
            args.putSerializable("pet", pet)
            f.arguments = args

            return f
        }
    }

    private lateinit var viewModel: EditPetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.edit_pet_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditPetViewModel::class.java)
        viewModel.pet = arguments?.getSerializable("pet") as Pet
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        initData()

        editPetPhoto.setOnClickListener { takePicture() }
        ivEditPetProfile.setOnClickListener{takePicture()}
        btnEditPetSalvar.setOnClickListener{updatePicture()}

    }


    private fun initData() {

        editPetName.setText(viewModel.pet!!.name)
        editPetDescription.setText(viewModel.pet!!.description)

        if (viewModel.pet!!.photoUrl.isEmpty()) {
            editPetPhoto.visibility = View.GONE
            ivEditPetProfile.visibility = View.VISIBLE
        }

        Glide.with(this)
            .load(viewModel.pet!!.photoUrl)
            //.centerCrop()
            .fitCenter()
            .override(680, 680)
            .into(editPetPhoto);
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
                if (viewModel.pet!!.namePhoto.isEmpty()) {
                    viewModel.pet!!.namePhoto = "${Calendar.getInstance().timeInMillis}.jpg"
                }
                editPetPhoto.visibility = View.VISIBLE
                ivEditPetProfile.visibility = View.GONE

                val bitmap = BitmapDrawable(resources, picture)
                editPetPhoto.setImageDrawable(bitmap)

            }
        }
    }

    private fun updatePicture() {


        if (editPetName.text.isNotEmpty() || editPetDescription.text.isNotEmpty()) {
            showProgress()
            viewModel.pet!!.name = editPetName.text.toString()
            viewModel.pet!!.description = editPetDescription.text.toString()

            if (::picture.isInitialized) {
                viewModel.updatePicture(picture, fusedLocationClient, viewModel.pet!!)
            } else {
                viewModel.update("", "", fusedLocationClient, viewModel.pet!!)
            }
        }
    }

    private fun showProgress() {
        viewModel.isLoader.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                editPetProgress_circular.visibility = View.VISIBLE
                editPetName.visibility = View.GONE
                editPetDescription.visibility = View.GONE
                editPetPhoto.visibility = View.GONE
                btnEditPetSalvar.visibility = View.GONE
            } else {
                val intent = Intent();
                intent.putExtra("pet", viewModel.pet!!)
                requireActivity().setResult(RESULT_OK,intent)
                requireActivity().finish()
            }
        })
    }

}
