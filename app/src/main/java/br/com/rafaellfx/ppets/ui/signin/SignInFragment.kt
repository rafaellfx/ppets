package br.com.rafaellfx.ppets.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.rafaellfx.ppets.MainActivity
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.SignUpActivity
import br.com.rafaellfx.ppets.databinding.SignInFragmentBinding


class SignInFragment : Fragment(R.layout.sign_in_fragment) {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.sign_in_fragment, container, false)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        val binding: SignInFragmentBinding =
            DataBindingUtil.setContentView(requireActivity(), R.layout.sign_in_fragment)
        binding.viewModel = viewModel

        binding.btnRegister.setOnClickListener {
            requireActivity().finish()
            startActivity(Intent(context, SignUpActivity::class.java))
        }

        binding.btnEnter.setOnClickListener {

                if (binding.editEmail.text.isEmpty() || binding.editSenha.text.isEmpty()) {
                    Toast.makeText(context, "E-mail ou senha incorreta, Tente novamente!", Toast.LENGTH_SHORT).show()
                }else{

                    viewModel.signIn(binding.editEmail.text.toString(), binding.editSenha.text.toString())

                    viewModel.isLogged.observe(viewLifecycleOwner, Observer {
                        viewModel.isLogged.value?.let {
                            if (it){
                                requireActivity().finish()
                                startActivity(Intent(context, MainActivity::class.java))
                            }else{
                                binding.editEmail.text = null
                                binding.editSenha.text = null
                                Toast.makeText(context, "E-mail ou senha incorreta, Tente novamente!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

                }

        }
    }

}
