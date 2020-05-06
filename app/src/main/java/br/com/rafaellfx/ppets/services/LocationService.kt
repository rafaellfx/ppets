package br.com.rafaellfx.ppets.services

import br.com.rafaellfx.ppets.model.Location
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot

class LocationService {

    companion object {

        val service = FireBaseService("locations")

        fun findAll(): Task<QuerySnapshot> {
            return service.findAll()
        }

        fun findId(document: String): DocumentReference {
            return service.findId(document)
        }

        fun save(location: Location): Task<DocumentReference> {
            return service.save(location)
        }

        fun update(location: Location) {
            service.update(location)
        }

        fun delete(location: Location) {
            service.delete(location)
        }

    }
}