package br.com.devcave.grpc.interceptors.client

import io.grpc.CallCredentials
import java.util.concurrent.Executor

class UserToken(private val jwt: String) : CallCredentials() {

    override fun applyRequestMetadata(requestInfo: RequestInfo, appExecutor: Executor, applier: MetadataApplier) {
        appExecutor.execute {
            applier.apply(MetaDataClient.userToken(jwt))
            // if something fails: applier.fail(status)
        }
    }

    override fun thisUsesUnstableApi() {
        // May changw in future
    }
}