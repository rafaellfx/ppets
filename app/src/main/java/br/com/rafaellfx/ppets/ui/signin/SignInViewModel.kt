package br.com.rafaellfx.ppets.ui.signin

import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.User
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    var user: User? = null

    fun singIn(email: String, pass: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, pass)

        return true
    }


}
