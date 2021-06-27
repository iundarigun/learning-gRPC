package br.com.devcave.grpc.devh.aggregator.domain

data class RecommendedMovie(
    val title: String,
    val year: Int,
    val rating: Double
)
