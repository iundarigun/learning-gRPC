package br.com.devcave.grpc.interceptors.client

import br.com.devcave.grpc.proto.greet.GreetRequest
import br.com.devcave.grpc.proto.greet.GreetServiceGrpc
import br.com.devcave.grpc.proto.greet.Greeting
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.MetadataUtils

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50055)
        .usePlaintext() // no secured
        .intercept(DeadLineInterceptor()) // This line apply deadline in all calls (is a good practice)
        .intercept(MetadataUtils.newAttachHeadersInterceptor(MetaDataClient.clientToken()))
        .build()

    runCatching { unaryCall(channel) }
        .onFailure { println("something is wrong ${it.message}") }

    runCatching { unaryCallWithUserToken(channel) }
        .onFailure { println("something is wrong ${it.message}") }

    channel.shutdown()
}

private fun unaryCallWithUserToken(channel: ManagedChannel) {
    val greetClient = GreetServiceGrpc.newBlockingStub(channel)

    val greeting = Greeting.newBuilder()
        .setFirstName("Oriol")
        .setLastName("Canalias")
        .build()
    val greetRequest = GreetRequest.newBuilder()
        .setGreeting(greeting)
        .build()

    val response = greetClient.withCallCredentials(UserToken("user-super-secret-token"))
        .greet(greetRequest)

    println("Return ${response.result}")
}

private fun unaryCall(channel: ManagedChannel) {
    val greetClient = GreetServiceGrpc.newBlockingStub(channel)

    val greeting = Greeting.newBuilder()
        .setFirstName("Oriol")
        .setLastName("Canalias")
        .build()
    val greetRequest = GreetRequest.newBuilder()
        .setGreeting(greeting)
        .build()

    val response = greetClient.greet(greetRequest)

    println("Return ${response.result}")
}
