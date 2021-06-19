package br.com.devcave.grpc.greeting.client

import br.com.devcave.grpc.proto.greet.GreetManyTimesRequest
import br.com.devcave.grpc.proto.greet.GreetRequest
import br.com.devcave.grpc.proto.greet.GreetServiceGrpc
import br.com.devcave.grpc.proto.greet.Greeting
import br.com.devcave.grpc.proto.greet.LongGreetRequest
import br.com.devcave.grpc.proto.greet.LongGreetResponse
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() // no secured
        .build()

    unaryCall(channel)

    serverStreamCall(channel)

    clientStreamCall(channel)

    channel.shutdown()
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

private fun serverStreamCall(channel: ManagedChannel) {
    val greetClient = GreetServiceGrpc.newBlockingStub(channel)

    val greeting = Greeting.newBuilder()
        .setFirstName("Oriol")
        .setLastName("Canalias")
        .build()
    val greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
        .setGreeting(greeting)
        .build()

    greetClient.greetManyTimes(greetManyTimesRequest)
        .forEachRemaining {
            println("result ${it.result}")
        }
}

private fun clientStreamCall(channel: ManagedChannel) {
    // Asyncrhonous client
    val greetClient = GreetServiceGrpc.newStub(channel)

    val latch = CountDownLatch(1)

    val requestObserver = greetClient.longGreet(object : StreamObserver<LongGreetResponse> {
        override fun onNext(value: LongGreetResponse) {
            // We get response from the server. Will call only once
            println("result ${value.result}")
        }

        override fun onError(t: Throwable) {
            // We ger an erro from the server
        }

        override fun onCompleted() {
            // The server is done sending data
            println("server completed")
            latch.countDown()
        }
    })

    for (it in 1..10) {
        val greeting = Greeting.newBuilder()
            .setFirstName("user $it")
            .setLastName("Canalias $it")
            .build()
        val request = LongGreetRequest.newBuilder()
            .setGreeting(greeting)
            .build()
        requestObserver.onNext(request)
    }

    requestObserver.onCompleted()

    latch.await(3L, TimeUnit.SECONDS)
}
