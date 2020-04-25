package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet(
    override var id: String,
    val name: String,
    val photos: ArrayList<String>,
    val locationId: String
) : Serializable, ModelInterface {


    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "photos" to photos.map { photo ->  photo },
            "locationId" to locationId
        )

    }
}