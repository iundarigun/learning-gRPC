package br.com.devcave.grpc.greeting.server

import io.grpc.ServerBuilder

fun main() {
    println("Hello gRPC")

    val server = ServerBuilder.forPort(50051)
        .addService(GreetServiceImpl())
        .build()

    server.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown request")
        server.shutdown()
        println("Successfully stopped the server")
    })

    server.awaitTermination()
}