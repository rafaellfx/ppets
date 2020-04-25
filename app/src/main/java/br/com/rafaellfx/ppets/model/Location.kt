package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Location(
    override var id: String,
    val latidute: String,
    val longitude: String
) : Serializable, ModelInterface {

    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "latitude" to latidute,
            "longitude" to longitude
        )
    }
}