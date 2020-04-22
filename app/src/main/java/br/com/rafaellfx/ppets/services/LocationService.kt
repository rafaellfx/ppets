package br.com.rafaellfx.ppets.services

import br.com.rafaellfx.ppets.model.Location
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class LocationService {

    companion object {

        val service = FireBaseService("locations")

        fun findAll(): Task<QuerySnapshot> {
            return service.findAll()
        }

        fun save(location: Location) {
            service.save(location)
        }

        fun update(location: Location) {
            service.update(location)
        }

        fun delete(location: Location) {
            service.delete(location)
        }

    }
}