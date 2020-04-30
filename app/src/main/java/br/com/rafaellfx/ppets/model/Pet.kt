package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet(
    override var id: String,
    val name: String,
    val description: String,
    var photoUrl: String,
    var namePhoto:String,
    val locationId: ArrayList<String>
) : Serializable, ModelInterface {


    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "photoUrl" to photoUrl,
            "namePhoto" to namePhoto,
            "description" to description,
            "locationId" to locationId
        )

    }
}