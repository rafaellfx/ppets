package br.com.rafaellfx.ppets.services

import android.util.Log
import br.com.rafaellfx.ppets.model.ModelInterface
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

open class FireBaseService(collection: String) {

    var firebase = FirebaseFirestore.getInstance().collection(collection)


    open fun findAll(): Task<QuerySnapshot> {

        return firebase.whereEqualTo("lost", false).get()
    }

    open fun find(param: String, value:Any): Task<QuerySnapshot> {
        return firebase.whereEqualTo(param, value).get()
    }

    open fun findId(document: String): DocumentReference {

        return firebase.document(document)
    }

    open fun save(obj: ModelInterface): Task<DocumentReference> {

        return this.firebase
            .add(obj.fromMap())
    }

    open fun update(obj: ModelInterface): Task<Void> {
        return this.firebase
            .document(obj.id)
            .update(obj.fromMap() as Map<String, Any>)

    }

    open fun delete(obj: ModelInterface) {
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

