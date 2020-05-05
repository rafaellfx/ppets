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
import androidx.navigation.fragment.findNavController
import br.com.rafaellfx.ppets.EditPetActivity
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.AboutPetFragmentBinding
import br.com.rafaellfx.ppets.model.Location
import br.com.rafaellfx.ppets.model.Pet
import br.com.rafaellfx.ppets.services.LocationService
import br.com.rafaellfx.ppets.services.PetsService
import br.com.rafaellfx.ppets.ui.editpet.EditPetFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.about_pet_fragment.*
import kotlinx.android.synthetic.main.new_pet_fragment.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class AboutPetFragment : Fragment() {

    val REQUEST_EDIT_PET = 1
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel = ViewModelProvider(this).get(AboutPetViewModel::class.java)

        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.about_pet_fragment)
        binding.viewModel = viewModel
        viewModel.pet = arguments?.getSerializable("pet") as Pet

        binding.btnEditar.setOnClickListener {
            val intent = Intent(context, EditPetActivity::class.java)
            intent.putExtra("pet", viewModel.pet)
            startActivityForResult(intent, REQUEST_EDIT_PET)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_EDIT_PET) {
            if (resultCode == Activity.RESULT_OK) {
                var pet = data!!.extras!!.get("pet") as Pet
               this.viewModel.pet = pet
                binding.viewModel = this.viewModel
            }
        }

    }


}
