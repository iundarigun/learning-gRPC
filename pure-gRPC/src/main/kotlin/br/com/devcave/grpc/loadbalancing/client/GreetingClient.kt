package br.com.devcave.grpc.loadbalancing.client

import br.com.devcave.grpc.proto.greet.GreetRequest
import br.com.devcave.grpc.proto.greet.GreetServiceGrpc
import br.com.devcave.grpc.proto.greet.Greeting
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.NameResolverRegistry

fun main() {
    println("Hello I am a gRPC Client")

    val channelLoadBalanceServerSide = ManagedChannelBuilder.forAddress("localhost", 8585)
        .usePlaintext() // no secured
        .build()
    unaryCall(channelLoadBalanceServerSide)

    ServiceRegistry.register("greeting-service", listOf("localhost:50061", "localhost:50062"))
    NameResolverRegistry.getDefaultRegistry().register(MyNameResolverProvider())
    val channelLoadBalanceClientSide = ManagedChannelBuilder.forTarget("greeting-service")
        .defaultLoadBalancingPolicy("round_robin") // Without this, first rpc strategy is applied
        .usePlaintext() // no secured
        .build()

    unaryCall(channelLoadBalanceClientSide)
}

private fun unaryCall(channel: ManagedChannel) {
    val greetClient = GreetServiceGrpc.newBlockingStub(channel)
    repeat(10) {
        Thread.sleep(500)
        val greeting = Greeting.newBuilder()
            .setFirstName("Iunda $it")
            .setLastName("iundarigun")
            .build()
        val greetRequest = GreetRequest.newBuilder()
            .setGreeting(greeting)
            .build()

        val response = greetClient.greet(greetRequest)

        println("Return ${response.result}")
    }
}