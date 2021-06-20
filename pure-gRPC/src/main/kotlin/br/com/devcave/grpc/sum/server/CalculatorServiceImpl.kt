package br.com.devcave.grpc.sum.server

import br.com.devcave.grpc.proto.calculator.CalculatorServiceGrpc
import br.com.devcave.grpc.proto.calculator.ComputeAverageRequest
import br.com.devcave.grpc.proto.calculator.ComputeAverageResponse
import br.com.devcave.grpc.proto.calculator.FindMaximumRequest
import br.com.devcave.grpc.proto.calculator.FindMaximumResponse
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeRequest
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeResponse
import br.com.devcave.grpc.proto.calculator.SquareRootRequest
import br.com.devcave.grpc.proto.calculator.SquareRootResponse
import br.com.devcave.grpc.proto.calculator.SumRequest
import br.com.devcave.grpc.proto.calculator.SumResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import kotlin.math.sqrt

class CalculatorServiceImpl : CalculatorServiceGrpc.CalculatorServiceImplBase() {
    override fun sum(request: SumRequest, responseObserver: StreamObserver<SumResponse>) {
        val result = request.operator1 + request.operator2
        val response = SumResponse.newBuilder()
            .setResult(result)
            .build()

        // Send the response
        responseObserver.onNext(response)

        // complete RPC call
        responseObserver.onCompleted()
    }

    override fun primeNumberDecompose(
        request: PrimeNumberDecomposeRequest,
        responseObserver: StreamObserver<PrimeNumberDecomposeResponse>
    ) {
        var primeNumber = 2
        var decomposable = request.operator
        while (decomposable > 1) {
            if (decomposable % primeNumber == 0) {
                responseObserver.onNext(
                    PrimeNumberDecomposeResponse.newBuilder()
                        .setResult(primeNumber)
                        .build()
                )
                decomposable /= primeNumber
            } else {
                primeNumber += 1
            }
        }
        // complete RPC call
        responseObserver.onCompleted()
    }

    override fun computeAverage(
        responseObserver: StreamObserver<ComputeAverageResponse>
    ): StreamObserver<ComputeAverageRequest> {
        return object : StreamObserver<ComputeAverageRequest> {
            val list = mutableListOf<Int>()

            override fun onNext(value: ComputeAverageRequest) {
                list.add(value.operator)
            }

            override fun onError(t: Throwable) {
                // On error
            }

            override fun onCompleted() {
                responseObserver.onNext(
                    ComputeAverageResponse.newBuilder()
                        .setResult(list.average())
                        .build()
                )
                responseObserver.onCompleted()
            }
        }
    }

    override fun findMaximum(
        responseObserver: StreamObserver<FindMaximumResponse>
    ): StreamObserver<FindMaximumRequest> {
        return object : StreamObserver<FindMaximumRequest> {
            var maxValue = 0

            override fun onNext(value: FindMaximumRequest) {
                if (value.operator > maxValue) {
                    maxValue = value.operator
                    responseObserver.onNext(
                        FindMaximumResponse.newBuilder()
                            .setResult(maxValue)
                            .build()
                    )
                }
            }

            override fun onError(t: Throwable) {
                // On error
            }

            override fun onCompleted() {
                responseObserver.onCompleted()
            }
        }
    }

    override fun squareRoot(request: SquareRootRequest, responseObserver: StreamObserver<SquareRootResponse>) {
        if (request.number >= 0) {
            responseObserver.onNext(
                SquareRootResponse.newBuilder()
                    .setResult(sqrt(request.number.toDouble()))
                    .build()
            )
            responseObserver.onCompleted()
        } else {
            // We construct the exception
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("The number is not positive")
                    .augmentDescription("number sent: ${request.number}")
                    .asRuntimeException()
            )
        }
    }
}