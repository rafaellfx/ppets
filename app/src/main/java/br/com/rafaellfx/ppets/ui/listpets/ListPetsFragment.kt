package br.com.rafaellfx.ppets.ui.listpets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.adapter.PetAdapter
import br.com.rafaellfx.ppets.extensions.navigateWithAnimations
import br.com.rafaellfx.ppets.model.Pet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.list_pets_fragment.*

class ListPetsFragment : Fragment(R.layout.list_pets_fragment) {

    private val viewAdapter by lazy { PetAdapter()}
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: ListPetsViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListPetsViewModel::class.java)

        viewManager = LinearLayoutManager(activity)

        rv_pets.layoutManager = viewManager

        rv_pets.adapter = viewAdapter
        observer()

        floatingActionButton.setOnClickListener { addPet() }

        viewModel.isDownload.observe(viewLifecycleOwner, Observer {check ->
            if (check) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
        })
    }


    private fun addPet() {
        findNavController().navigateWithAnimations(R.id.nav_new_Pet_Fragment)
    }

    private fun observer() {
        //LiveData
        viewModel.listPets.observe(viewLifecycleOwner, Observer { item: List<Pet> ->
            viewAdapter.update(item)
        })

        context?.let {
            viewModel.loadPets(it)
        }

    }

}
