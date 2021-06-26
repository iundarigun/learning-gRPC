package br.com.devcave.grpc.loadbalancing.server

import io.grpc.ServerBuilder

fun main() {
    println("Hello gRPC")

    val server1 = ServerBuilder.forPort(50061)
        .addService(GreetServiceImpl("server1"))
        .build()

    val server2 = ServerBuilder.forPort(50062)
        .addService(GreetServiceImpl("server2"))
        .build()

    server1.start()
    server2.start()

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Shutdown request")
        server1.shutdown()
        server2.shutdown()
        println("Successfully stopped the server")
    })

    server1.awaitTermination()
    server2.awaitTermination()
}