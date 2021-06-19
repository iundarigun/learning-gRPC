package br.com.devcave.grpc.sum.server

import io.grpc.ServerBuilder

fun main() {
    println("Hello gRPC")

    val server = ServerBuilder.forPort(50052)
        .addService(CalculatorServiceImpl())
        .build()

    server.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown request")
        server.shutdown()
        println("Successfully stopped the server")
    })

    server.awaitTermination()
}