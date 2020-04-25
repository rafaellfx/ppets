package br.com.rafaellfx.ppets.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafaellfx.ppets.model.User
import br.com.rafaellfx.ppets.services.UserService
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {

    var user: User? = null
    var registered: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun signUp(){

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user!!.email, user!!.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    user!!.id = auth.currentUser!!.uid
                    UserService.save(user!!)
                    registered.value = true
                }else{
                    Log.d("teste", task.exception.toString())
                    registered.value = false
                }
            }
    }

}
