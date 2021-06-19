package br.com.devcave.grpc.greeting.client

import br.com.devcave.grpc.proto.dummy.DummyServiceGrpc
import io.grpc.ManagedChannelBuilder

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() // no secured
        .build()
    val syncClient = DummyServiceGrpc.newBlockingStub(channel)
//    val asyncClient = DummyServiceGrpc.newFutureStub(channel)
    println("Do someting with syncClient")

    channel.shutdown()
}