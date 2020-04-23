package br.com.rafaellfx.ppets.model

interface ModelInterface {
    var id: String
    fun fromMap(): HashMap<Any, Any>
}