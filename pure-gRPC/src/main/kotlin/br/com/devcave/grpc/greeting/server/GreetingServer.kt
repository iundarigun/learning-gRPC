package br.com.devcave.grpc.greeting.server

import io.grpc.ServerBuilder
import java.io.File

fun main() {
    println("Hello gRPC")

    val server = ServerBuilder.forPort(50051)
        .addService(GreetServiceImpl())
        .build()

    val secureServer = ServerBuilder.forPort(50053)
        .addService(GreetServiceImpl())
        .useTransportSecurity(File("ssl/server.crt"),
        File("ssl/server.pem"))
        .build()

    server.start()
    secureServer.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown request")
        server.shutdown()
        secureServer.shutdown()
        println("Successfully stopped the server")
    })

    server.awaitTermination()
    secureServer.awaitTermination()
}