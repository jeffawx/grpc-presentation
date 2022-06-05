package com.airwallex.demo

import com.airwallex.grpc.annotations.GrpcClient
import com.github.michaelbull.result.get
import demo.EchoServiceRpc
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    "grpc.server.name=test",
    "grpc.server.port=-1", // -1 for in-process server
    "grpc.client.channels.echo.in-process=true",
    "grpc.client.channels.echo.server-name=test"
)
class EchoServiceTest {

    @Autowired
    @GrpcClient("echo")
    private lateinit var echoClient: EchoServiceRpc

    @Test
    fun echoTest() = runBlocking<Unit> {
        assertThat(echoClient.echo("Hello").get())
            .isEqualTo("echo from server: Hello")
    }
}
