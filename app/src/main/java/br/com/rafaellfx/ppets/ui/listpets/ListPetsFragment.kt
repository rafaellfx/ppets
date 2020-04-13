package br.com.rafaellfx.ppets.ui.listpets

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.rafaellfx.ppets.R

class ListPetsFragment : Fragment() {

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
        viewModel = ViewModelProviders.of(this).get(ListPetsViewModel::class.java)

        Log.e("teste", "viewModel ${viewModel.toString()}")
    }

}
