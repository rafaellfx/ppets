package br.com.rafaellfx.ppets.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import br.com.rafaellfx.ppets.R
import br.com.rafaellfx.ppets.databinding.SignUpFragmentBinding
import br.com.rafaellfx.ppets.model.User

class SignUpFragment : Fragment(R.layout.sign_up_fragment) {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.sign_up_fragment, container, false)
//    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        val binding: SignUpFragmentBinding =
            DataBindingUtil.setContentView(requireActivity(), R.layout.sign_up_fragment)

        binding.btnRegister.setOnClickListener {
            if (binding.editEmail.text.isEmpty() || binding.editSenha.text.isEmpty() ||
                binding.editName.text.isEmpty() || binding.editPhone.text.isEmpty()) {

                Toast.makeText(context, "Todos os dados são obrigatórios!", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.user = User("",
                    "",
                    binding.editPhone.text.toString(),
                    binding.editName.text.toString(),
                    binding.editEmail.text.toString(),
                    binding.editSenha.text.toString())
                viewModel.signUp()
            }
        }

        viewModel.registered.observe(viewLifecycleOwner, Observer {
            viewModel.registered.value?.let {
                if (it){
//                    requireActivity().finish()
//                    startActivity(Intent(context, ListPets::class.java))
                    findNavController().navigate(R.id.listPetFragment)
                }else{
                    Toast.makeText(context, "Este E-mail já esta cadastrado!", Toast.LENGTH_LONG).show()
                }
            }
        })

    }

}
