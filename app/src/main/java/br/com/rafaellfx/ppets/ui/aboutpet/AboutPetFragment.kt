package br.com.rafaellfx.ppets.ui.aboutpet

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.AboutPetFragmentBinding
import br.com.rafaellfx.ppets.extensions.navigateWithAnimations
import br.com.rafaellfx.ppets.extensions.navigateWithAnimationsDestinations
import br.com.rafaellfx.ppets.model.Pet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.about_pet_fragment.*

class AboutPetFragment : Fragment() {

    val REQUEST_EDIT_PET = 1
    lateinit var picture: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: AboutPetFragmentBinding


    companion object {
        fun newInstance() = AboutPetFragment()
    }

    private lateinit var viewModel: AboutPetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.about_pet_fragment, container, false)
        viewModel = ViewModelProvider(this).get(AboutPetViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

       // binding.viewModel = viewModel
        val safeArgs: AboutPetFragmentArgs by navArgs()
        viewModel.pet = safeArgs.pet

        binding.btnEditar.setOnClickListener {
//            val intent = Intent(context, EditPetActivity::class.java)
//            intent.putExtra("pet", viewModel.pet)
//            startActivityForResult(intent, REQUEST_EDIT_PET)
            val directionsEdit = AboutPetFragmentDirections.actionAboutPetFragmentToEditPetFragment(safeArgs.pet)
            findNavController().navigateWithAnimationsDestinations(directionsEdit)
        }

        binding.btnViewMaps.setOnClickListener {
//            val intent = Intent(context, MapsActivity::class.java)
//            intent.putExtra("pet", viewModel.pet)
//            startActivity(intent)
            val directionsMap = AboutPetFragmentDirections.actionAboutPetFragmentToMapsActivity(safeArgs.pet)
             findNavController().navigateWithAnimationsDestinations(directionsMap)
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
