package br.com.devcave.grpc.interceptors.server

import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

class AuthInterceptor : ServerInterceptor {

    private val clientTokenKey = Metadata.Key.of("client-token", Metadata.ASCII_STRING_MARSHALLER)
    private val userTokenKey = Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER)


    override fun <ReqT : Any, RespT : Any> interceptCall(
        serverCall: ServerCall<ReqT, RespT>,
        metadata: Metadata,
        serverCallHandler: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val clientToken = metadata.get(clientTokenKey)
        val userToken = metadata.get(userTokenKey)

        if (validateClient(clientToken) && validateUser(userToken)) {
            val context = Context.current().withValue(userTypeKey, "ADMIN")
//            return serverCallHandler.startCall(serverCall, metadata)
            // We use the second sentence to send context with new key
            return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler)
        } else {
            val status = Status.UNAUTHENTICATED.withDescription("Invalid token")
            serverCall.close(status, metadata)
        }
        return object : ServerCall.Listener<ReqT>() {}
    }

    private fun validateClient(clientToken: String?): Boolean =
        clientToken == "greeting-super-secret-token"

    private fun validateUser(userToken: String?): Boolean =
        userToken == "user-super-secret-token"

    companion object {
        // This object is used to save, so we need to use to save and to retrieve.
        val userTypeKey: Context.Key<String> = Context.key("user-type")
    }
}