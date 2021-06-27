package br.com.devcave.grpc.devh.aggregator.domain

data class UserRequest(
    val loginId: String,
    val genre: String
)
