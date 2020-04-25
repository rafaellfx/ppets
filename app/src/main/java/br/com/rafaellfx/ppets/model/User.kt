package br.com.rafaellfx.ppets.model

import java.io.Serializable

class User (
    override var id: String,
    val locationId: String,
    val phone: String,
    val name: String,
    val email: String,
    val password: String
) : Serializable, ModelInterface {

    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "locationId" to locationId,
            "phone" to phone,
            "name" to name
            )
    }
}