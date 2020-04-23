package br.com.rafaellfx.ppets.services

import android.util.Log
import com.firebase.ui.auth.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

open class FireBaseService(collection: String) {

    protected val firebase = FirebaseFirestore.getInstance().collection(collection)


    open fun findAll(): Task<QuerySnapshot> {

        return firebase.get()
    }

    open fun save(obj: User) {
        firebase
            .add(obj.fromMap())
            .addOnSuccessListener { documentReference ->
                Log.d("LOGTESTE", "${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("LOGTESTE", "Error ", e)
            }
    }

    open fun update(obj: User) {
        firebase.document(obj.id)
            .update(obj.fromMap() as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
                Log.d("LOGTESTE", "${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("location", "Error ", e)
            }
    }

    open fun delete(obj: User) {
        firebase.document(obj.id)
            .delete()
            .addOnSuccessListener { documentReference ->
                Log.d("LOGTESTE", "Feito ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("LOGTESTE", "Error ", e)
            }
    }
}