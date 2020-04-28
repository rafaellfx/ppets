package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Location(
    override var id: String,
    val latidute: Double,
    val longitude: Double
) : Serializable, ModelInterface {

    override fun fromMap(): HashMap<String, Any> {
        return hashMapOf(
            "latitude" to latidute,
            "longitude" to longitude
        )
    }
}