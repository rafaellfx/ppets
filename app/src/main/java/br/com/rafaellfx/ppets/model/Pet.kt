package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet(
    override var id: String,
    val name: String,
    val description: String,
    val photo: String,
    val locationId: ArrayList<String>
) : Serializable, ModelInterface {


    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "photo" to photo,
            "description" to description,
            "locationId" to locationId
        )

    }
}