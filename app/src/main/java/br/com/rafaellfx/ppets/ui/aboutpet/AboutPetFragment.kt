package br.com.rafaellfx.ppets.ui.aboutpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.AboutPetFragmentBinding
import br.com.rafaellfx.ppets.model.Pet

class AboutPetFragment : Fragment() {

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

        val binding: AboutPetFragmentBinding =
            DataBindingUtil.setContentView(activity!!, R.layout.about_pet_fragment)
        binding.viewModel = viewModel

        viewModel.pet = arguments?.getSerializable("pet") as Pet

    }

}
