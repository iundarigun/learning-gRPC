package br.com.devcave.grpc.loadbalancing.client

import io.grpc.NameResolver
import io.grpc.NameResolverProvider
import java.net.URI

class MyNameResolver(private val service: String) : NameResolver() {

    override fun start(listener: Listener2) {
        val resolutionResult = ResolutionResult.newBuilder()
            .setAddresses(ServiceRegistry.getInstances(service))
            .build()
        listener.onResult(resolutionResult)
    }

    override fun getServiceAuthority(): String {
        return "temp"
    }

    override fun shutdown() {
    }
}

class MyNameResolverProvider : NameResolverProvider() {
    override fun newNameResolver(targetUri: URI, args: NameResolver.Args): NameResolver {
        println("Looking for service $targetUri")
        return MyNameResolver(targetUri.toString())
    }

    override fun getDefaultScheme(): String = "dns"

    override fun isAvailable(): Boolean = true

    override fun priority(): Int = 5
}