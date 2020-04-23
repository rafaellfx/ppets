package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet(
    override var id: String,
    val name: String,
    val photos: ArrayList<String>,
    val locationId: String
) : Serializable, ModelInterface {


    override fun fromMap(): HashMap<Any, Any> {

        val pro = hashMapOf<Any,Any>(
            "id" to this.id,
            "name" to this.name,
            "photos" to this.photos.map { photo ->  photo },
            "locationId" to this.locationId
        )

        return pro

    }
}