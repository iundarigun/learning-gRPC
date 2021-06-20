package br.com.devcave.grpc.greeting.client

import br.com.devcave.grpc.proto.greet.GreetEveryoneRequest
import br.com.devcave.grpc.proto.greet.GreetEveryoneResponse
import br.com.devcave.grpc.proto.greet.GreetManyTimesRequest
import br.com.devcave.grpc.proto.greet.GreetRequest
import br.com.devcave.grpc.proto.greet.GreetServiceGrpc
import br.com.devcave.grpc.proto.greet.GreetWithDeadlineRequest
import br.com.devcave.grpc.proto.greet.Greeting
import br.com.devcave.grpc.proto.greet.LongGreetRequest
import br.com.devcave.grpc.proto.greet.LongGreetResponse
import io.grpc.Deadline
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.StreamObserver
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .usePlaintext() // no secured
        .build()

    val securedChannel = NettyChannelBuilder.forAddress("localhost", 50053)
        .sslContext(GrpcSslContexts.forClient().trustManager(File("ssl/ca.crt")).build())
        .build()

    unaryCall(securedChannel)

    serverStreamCall(channel)

    clientStreamCall(channel)

    bidirectionalStreamCall(channel)

    greetingWithDeadline(channel, 500)

    greetingWithDeadline(channel, 200)

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

private fun bidirectionalStreamCall(channel: ManagedChannel) {
    // Asyncrhonous client
    val greetClient = GreetServiceGrpc.newStub(channel)

    val latch = CountDownLatch(1)

    val requestObserver = greetClient.greetEveryone(object : StreamObserver<GreetEveryoneResponse> {
        override fun onNext(value: GreetEveryoneResponse) {
            // We get response from the server. Will call many times
            println("result ${value.result}")
        }

        override fun onError(t: Throwable) {
            // We ger an erro from the server
            latch.countDown()
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
        val request = GreetEveryoneRequest.newBuilder()
            .setGreeting(greeting)
            .build()
        requestObserver.onNext(request)
    }

    requestObserver.onCompleted()

    latch.await(3L, TimeUnit.SECONDS)
}

private fun greetingWithDeadline(channel: ManagedChannel, timeoutInMs: Long) {
    val greetingClient = GreetServiceGrpc.newBlockingStub(channel)

    runCatching {
        greetingClient.withDeadline(Deadline.after(timeoutInMs, TimeUnit.MILLISECONDS))
            .greetWithDeadline(
                GreetWithDeadlineRequest.newBuilder()
                    .setGreeting(Greeting.newBuilder().setFirstName("Uri").build())
                    .build()
            )
    }.onFailure {
        println(it.javaClass)
        if (it is StatusRuntimeException && it.status.code == Status.DEADLINE_EXCEEDED.code) {
            println("Deadline exceeded, we don't need response anymore: ${it.message}")
        } else {
            println("Other error: ${it.message}")
        }
    }.onSuccess {
        println("response for deadline $timeoutInMs is ${it.result}")
    }
}
