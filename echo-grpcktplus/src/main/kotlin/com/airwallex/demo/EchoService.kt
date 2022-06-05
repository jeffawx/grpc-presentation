package com.airwallex.demo

import com.airwallex.grpc.error.Error
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import demo.EchoServiceRpc
import org.springframework.stereotype.Service

@Service
class EchoService : EchoServiceRpc {

    override suspend fun echo(request: String): Result<String, Error> {
        return Ok("echo from server: $request")
    }
}
