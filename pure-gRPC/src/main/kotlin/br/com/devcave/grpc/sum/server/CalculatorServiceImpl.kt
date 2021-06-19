package br.com.devcave.grpc.sum.server

import br.com.devcave.grpc.proto.calculator.CalculatorServiceGrpc
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeRequest
import br.com.devcave.grpc.proto.calculator.PrimeNumberDecomposeResponse
import br.com.devcave.grpc.proto.calculator.SumRequest
import br.com.devcave.grpc.proto.calculator.SumResponse
import io.grpc.stub.StreamObserver

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
}