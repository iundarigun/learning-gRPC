package br.com.devcave.grpc.sum.client

import br.com.devcave.grpc.proto.calculator.CalculatorServiceGrpc
import br.com.devcave.grpc.proto.calculator.ComputeAverageRequest
import br.com.devcave.grpc.proto.calculator.ComputeAverageResponse
import br.com.devcave.grpc.proto.calculator.FindMaximumRequest
import br.com.devcave.grpc.proto.calculator.FindMaximumResponse
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeRequest
import br.com.devcave.grpc.proto.calculator.SquareRootRequest
import br.com.devcave.grpc.proto.calculator.SumRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun main() {
    println("Hello I am a gRPC Client")

    val channel = ManagedChannelBuilder.forAddress("localhost", 50052)
        .usePlaintext() // no secured
        .build()

    sum(channel, 5, 15)
    sum(channel, 15, -10)

    decompose(channel, 120)
    decompose(channel, 422407558)

    average(channel, 2, 6, 4, 8, 9)
    average(channel, 2, 4, 4, 6)

    findMaximum(channel, 2, 3, 1, 5, 10, 6, 3, 21)

    sqrt(channel, 25)
    sqrt(channel, 10)
    sqrt(channel, -1)

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

private fun average(channel: ManagedChannel, vararg operators: Int) {
    val calculatorClient = CalculatorServiceGrpc.newStub(channel)

    val latch = CountDownLatch(1)

    val requestObserver = calculatorClient.computeAverage(
        object : StreamObserver<ComputeAverageResponse> {
            override fun onNext(value: ComputeAverageResponse) {
                println("average of $operators is ${value.result}")
            }

            override fun onError(t: Throwable?) {
                // onError
            }

            override fun onCompleted() {
                latch.countDown()
                println("finalize")
            }
        }
    )
    operators.forEach {
        requestObserver.onNext(ComputeAverageRequest.newBuilder().setOperator(it).build())
    }
    requestObserver.onCompleted()

    latch.await(3, TimeUnit.SECONDS)
}

private fun findMaximum(channel: ManagedChannel, vararg operators: Int) {
    val calculatorClient = CalculatorServiceGrpc.newStub(channel)

    val latch = CountDownLatch(1)

    val requestObserver = calculatorClient.findMaximum(
        object : StreamObserver<FindMaximumResponse> {
            override fun onNext(value: FindMaximumResponse) {
                println("maximum value for now: ${value.result}")
            }

            override fun onError(t: Throwable?) {
                // onError
            }

            override fun onCompleted() {
                latch.countDown()
                println("finalize")
            }
        }
    )
    operators.forEach {
        requestObserver.onNext(FindMaximumRequest.newBuilder().setOperator(it).build())
    }
    requestObserver.onCompleted()

    latch.await(3, TimeUnit.SECONDS)
}

private fun sqrt(channel: ManagedChannel, number: Int) {
    val calculatorClient = CalculatorServiceGrpc.newBlockingStub(channel)
    runCatching {
        calculatorClient.squareRoot(
            SquareRootRequest.newBuilder()
                .setNumber(number)
                .build()
        )
    }.onFailure {
        println("got an exception!")
        println(it.message)
//        it.printStackTrace()
    }.onSuccess { println("result ${it.result}") }
}