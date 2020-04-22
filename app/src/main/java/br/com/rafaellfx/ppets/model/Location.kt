package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Location(
    override var id: String,
    val latidute: String,
    val longitude: String
) : Serializable, ModelInterface {

    override fun fromMap(): HashMap<String, String> {
        var local = hashMapOf(
            "id" to this.id,
            "latitude" to this.latidute,
            "longitude" to this.longitude
        )
        return local
    }
}