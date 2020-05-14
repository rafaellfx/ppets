package br.com.rafaellfx.ppets.ui.lost

import android.os.Bundle
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
import kotlinx.android.synthetic.main.lost_fragment.*

class LostFragment : Fragment() {


    private lateinit var viewModel: LostViewModel

    private val viewAdapter by lazy { PetAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.lost_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LostViewModel::class.java)


        rvLost.layoutManager = LinearLayoutManager(activity)

        rvLost.adapter = viewAdapter

        observe()


        viewModel.isDownload.observe(viewLifecycleOwner, Observer { check ->
            if (check) pgbLost.visibility = View.VISIBLE else pgbLost.visibility = View.GONE
        })
    }

    private fun observe() {
        viewModel.listPetsLost.observe(viewLifecycleOwner, Observer { listPet ->
            txtPetsLost.visibility = View.GONE
            if (listPet.isNotEmpty()) {
                viewAdapter.update(listPet)
            } else {
                txtPetsLost.visibility = View.VISIBLE
            }
        })

        context?.let {
            viewModel.loadPetLost(it)
        }
    }

}
