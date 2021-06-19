package br.com.devcave.grpc.sum.client

import br.com.devcave.grpc.proto.calculator.CalculatorServiceGrpc
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeRequest
import br.com.devcave.grpc.proto.calculator.SumRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() // no secured
        .build()

    sum(channel, 5, 15)
    sum(channel, 15, -10)
    decompose(channel, 120)
    decompose(channel, 422407558)

    channel.shutdown()
}

private fun sum(channel: ManagedChannel, operator1: Int, operator2: Int) {
    val calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel)

    val sumRequest = SumRequest.newBuilder()
        .setOperator1(operator1)
        .setOperator2(operator2)
        .build()

    val response = calculatorClient.sum(sumRequest)

    println("result of sum $operator1 and $operator2 is ${response.result}")
}

private fun decompose(channel: ManagedChannel, operator: Int) {
    val calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel)

    val request = PrimeNumberDecomposeRequest.newBuilder()
        .setOperator(operator)
        .build()

    println("decomposing $operator: ")
    calculatorClient.primeNumberDecompose(request)
        .forEachRemaining {
            print("${it.result}, ")
        }
    println()
}