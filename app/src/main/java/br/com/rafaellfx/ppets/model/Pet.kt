package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet(
    override var id: String,
    var name: String,
    var description: String,
    var photoUrl: String,
    var namePhoto:String,
    var lost: Boolean,
    val locationId: ArrayList<String>
) : Serializable, ModelInterface {


    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "photoUrl" to photoUrl,
            "namePhoto" to namePhoto,
            "description" to description,
            "lost" to lost,
            "locationId" to locationId
        )

    }
}