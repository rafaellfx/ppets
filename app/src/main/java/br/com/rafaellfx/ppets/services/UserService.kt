package br.com.rafaellfx.ppets.services

import android.util.Log
import br.com.rafaellfx.ppets.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class UserService {

    companion object {
        val service = FireBaseService("users")

        fun findAll(): Task<QuerySnapshot> = service.findAll()

        fun save(user: User) {
            service.firebase
                .document(user.id)
                .set(user.fromMap())
                .addOnSuccessListener { documentReference ->
                    Log.d("LOGTESTE", "${documentReference}")
                }
                .addOnFailureListener { e ->
                    Log.w("LOGTESTE", "Error ", e)
                }
        }

        fun update(user: User) {
            service.update(user)
        }

        fun delete(user: User) {
            service.delete(user)
        }

    }
}