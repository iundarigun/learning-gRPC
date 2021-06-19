package br.com.devcave.grpc.greeting.server

import br.com.devcave.grpc.proto.greet.GreetManyTimesRequest
import br.com.devcave.grpc.proto.greet.GreetManyTimesResponse
import br.com.devcave.grpc.proto.greet.GreetRequest
import br.com.devcave.grpc.proto.greet.GreetResponse
import br.com.devcave.grpc.proto.greet.GreetServiceGrpc
import br.com.devcave.grpc.proto.greet.LongGreetRequest
import br.com.devcave.grpc.proto.greet.LongGreetResponse
import io.grpc.stub.StreamObserver

class GreetServiceImpl : GreetServiceGrpc.GreetServiceImplBase() {

    override fun greet(request: GreetRequest, responseObserver: StreamObserver<GreetResponse>) {
        val greeting = request.greeting

        val result = "Hello ${greeting.firstName}!"
        val response = GreetResponse.newBuilder()
            .setResult(result)
            .build()

        // Send the response
        responseObserver.onNext(response)

        // complete RPC call
        responseObserver.onCompleted()
    }

    override fun greetManyTimes(
        request: GreetManyTimesRequest,
        responseObserver: StreamObserver<GreetManyTimesResponse>
    ) {
        val greeting = request.greeting

        runCatching {
            for (it in 1..10) {
                val result = "Hello ${greeting.firstName}, response number $it"
                val response = GreetManyTimesResponse.newBuilder()
                    .setResult(result)
                    .build()
                responseObserver.onNext(response)
                Thread.sleep(1_000L)
            }
        }.onFailure {
            println(it.message)
            it.printStackTrace()
        }

        // complete RPC call
        responseObserver.onCompleted()
    }

    override fun longGreet(
        responseObserver: StreamObserver<LongGreetResponse>
    ): StreamObserver<LongGreetRequest> {

        return object : StreamObserver<LongGreetRequest> {
            val result = StringBuilder()

            override fun onNext(value: LongGreetRequest) {
                // Client sent a message
                println("received ${value.greeting.firstName}")
                result.append("Hello ${value.greeting.firstName}!, ")
            }

            override fun onError(t: Throwable) {
                // Client sent an error
            }

            override fun onCompleted() {
                // Client is done
                responseObserver.onNext(
                    LongGreetResponse.newBuilder()
                        .setResult(result.toString())
                        .build()
                )
                responseObserver.onCompleted()
            }
        }
    }
}