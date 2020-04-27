package br.com.rafaellfx.ppets.ui.listpets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.NewPetActivity
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.SignInActivity
import br.com.rafaellfx.ppets.adapter.PetAdapter
import kotlinx.android.synthetic.main.list_pets_fragment.*

class ListPetsFragment : Fragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: ListPetsViewModel

    companion object {
        fun newInstance() = ListPetsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_pets_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListPetsViewModel::class.java)

        if(!viewModel.isLoggedIn()){
            activity!!.finish()
            startActivity(Intent(context, SignInActivity::class.java))
        }

        viewManager = LinearLayoutManager(activity)

        rv_pets.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }
        floatingActionButton.setOnClickListener { addPet() }
    }

    override fun onStart() {
        super.onStart()
        observer()
    }

    fun addPet(){
        startActivity(Intent(context, NewPetActivity::class.java))
    }

    private fun observer(){
        //LiveData
        viewModel.listPets.observe(viewLifecycleOwner, Observer {

            viewModel.listPets.value?.let {

                viewAdapter = PetAdapter(it, activity as Context)
                rv_pets.adapter = viewAdapter
            }
        })
        viewModel.getPets()
    }

}
