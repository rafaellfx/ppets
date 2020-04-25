package br.com.rafaellfx.ppets.ui.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.User
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    var user: User? = null
    var isLogged: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun singIn(email: String, pass: String) {

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                     isLogged.value = true
                }else {
                    Log.d("teste", task.exception.toString())
                    isLogged.value = false
                }
            }

    }


}
