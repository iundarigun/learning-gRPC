package br.com.devcave.grpc.devh.entity

data class Movie(
    val id: Int,
    val name: String,
    val year: Int,
    val rating: Double,
    val genre: String
)
