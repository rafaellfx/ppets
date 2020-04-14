package br.com.rafaellfx.ppets.ui.listpets

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.adapter.PetAdapter
import kotlinx.android.synthetic.main.list_pets_fragment.*

class ListPetsFragment : Fragment() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    companion object {
        fun newInstance() = ListPetsFragment()
    }

    private lateinit var viewModel: ListPetsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.list_pets_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListPetsViewModel::class.java)

        viewManager = LinearLayoutManager(activity)

        rv_pets.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        viewModel.listPets.observe(viewLifecycleOwner, Observer {
            viewModel.listPets.value?.let {
                viewAdapter = PetAdapter(it, activity as Context)
                rv_pets.adapter = viewAdapter
            }
        })
        viewModel.getPets()

        Log.e("teste", "viewModel ${viewModel.toString()}")
    }

}
