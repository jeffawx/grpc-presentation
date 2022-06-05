package com.airwallex.demo

import com.google.protobuf.StringValue
import demo.EchoServiceGrpcKt
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class EchoService : EchoServiceGrpcKt.EchoServiceCoroutineImplBase() {

    override suspend fun echo(request: StringValue): StringValue {
        return StringValue.of("echo from server: ${request.value}")
    }
}
