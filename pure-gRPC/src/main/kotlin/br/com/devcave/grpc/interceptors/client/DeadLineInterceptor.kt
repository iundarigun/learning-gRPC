package br.com.devcave.grpc.interceptors.client

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.Deadline
import io.grpc.MethodDescriptor
import java.util.concurrent.TimeUnit

class DeadLineInterceptor : ClientInterceptor {
    override fun <ReqT : Any, RespT : Any> interceptCall(
        methodDescriptor: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        channel: Channel
    ): ClientCall<ReqT, RespT> {
        // If some call has an specific deadline time, we keep it
        val deadLine = callOptions.deadline ?: Deadline.after(4, TimeUnit.SECONDS)

        return channel.newCall(
            methodDescriptor,
            callOptions.withDeadline(deadLine)
        )
    }
}