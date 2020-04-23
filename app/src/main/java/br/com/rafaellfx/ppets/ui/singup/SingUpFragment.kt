package br.com.rafaellfx.ppets.ui.singup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.SingUpFragmentBinding

class SingUpFragment : Fragment() {

    companion object {
        fun newInstance() = SingUpFragment()
    }

    private lateinit var viewModel: SingUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.sing_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SingUpViewModel::class.java)

        val binding: SingUpFragmentBinding =
            DataBindingUtil.setContentView(activity!!, R.layout.sing_up_fragment)
    }

}
