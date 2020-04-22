package br.com.rafaellfx.ppets.services

import android.util.Log
import br.com.rafaellfx.ppets.model.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class PetsService {

    companion object {
        val service = FireBaseService("pets")

        fun findAll(): Task<QuerySnapshot> = service.findAll()

        fun save(pet: Pet) {
            service.save(pet)
        }

        fun update(pet: Pet) {
            service.update(pet)
        }

        fun delete(pet: Pet) {
            service.delete(pet)
        }

    }
}