package br.com.rafaellfx.ppets.model

import java.io.Serializable

class User (
    override var id: String,
    val locationId: String,
    val name: String
) : Serializable, ModelInterface {

    override fun fromMap(): HashMap<Any, Any> {
        var user = hashMapOf<Any, Any>(
            "id" to this.id,
            "locationId" to this.locationId,
            "name" to this.name

            )
        return user
    }
}