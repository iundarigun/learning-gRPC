package br.com.devcave.grpc.loadbalancing.client

import io.grpc.EquivalentAddressGroup
import java.net.InetSocketAddress

object ServiceRegistry {
    val map = mutableMapOf<String, List<EquivalentAddressGroup>>()

    fun register(service: String, instances: List<String>) {
        map[service] = instances.map { it.split(":") }
            .map { EquivalentAddressGroup(InetSocketAddress(it[0], it[1].toInt())) }
    }

    fun getInstances(service: String): List<EquivalentAddressGroup> =
        map[service] ?: emptyList()
}