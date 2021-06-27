package br.com.devcave.grpc.devh.entity

data class User(
    val login: String,
    val name: String,
    var genre: String
)
