package br.com.devcave.grpc.interceptors.client

import io.grpc.Metadata

object MetaDataClient {
    fun clientToken(): Metadata {
        return Metadata().also {
            it.put(
                Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER),
                "greeting-super-secret-token"
            )
        }
    }

    fun userToken(token: String): Metadata {
        return Metadata().also {
            it.put(
                Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER),
                token
            )
        }
    }
}