package br.com.rafaellfx.ppets.model

import java.io.Serializable

data class Pet (
    val uid: String,
    val name: String,
    val photo: ArrayList<String>,
    val lat: String,
    val long: String) : Serializable