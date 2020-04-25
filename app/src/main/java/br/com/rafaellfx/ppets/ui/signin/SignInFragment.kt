package br.com.rafaellfx.ppets.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.SignUpActivity
import br.com.rafaellfx.ppets.databinding.SignInFragmentBinding


class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        val binding: SignInFragmentBinding = DataBindingUtil.setContentView(activity!!, R.layout.sign_in_fragment)
        binding.viewModel = viewModel

        binding.btnRegister.setOnClickListener {
            activity!!.finish()
            startActivity(Intent(context, SignUpActivity::class.java))
        }

        binding.btnEnter.setOnClickListener {
            viewModel.singIn(binding.editEmail.text.toString(), binding.editSenha.text.toString())
        }
    }

}
