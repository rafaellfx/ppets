package br.com.rafaellfx.ppets.services

import br.com.rafaellfx.ppets.model.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

class PetsService {

    companion object {
        val service = FireBaseService("pets")

        fun findAll(): Task<QuerySnapshot> = service.findAll()

        fun find(param: String = "lost", value: Boolean = true): Task<QuerySnapshot> = service.find(param, value)

        fun save(pet: Pet): Task<DocumentReference> {
           return service.save(pet)
        }

        fun update(pet: Pet): Task<Void> {
            return service.update(pet)
        }

        fun delete(pet: Pet) {
            service.delete(pet)
        }

    }
}