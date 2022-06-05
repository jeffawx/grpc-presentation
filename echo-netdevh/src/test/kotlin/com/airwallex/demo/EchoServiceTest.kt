package com.airwallex.demo

import com.google.protobuf.StringValue
import demo.EchoServiceGrpcKt
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import net.devh.boot.grpc.client.inject.GrpcClient
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    "grpc.server.in-process-name=test",
    "grpc.client.echo.address=in-process:test"
)
class EchoServiceTest {

    @GrpcClient("echo")
    private lateinit var echoClient: EchoServiceGrpcKt.EchoServiceCoroutineStub

    @Test
    fun echoTest() = runBlocking<Unit> {
        assertThat(echoClient.echo(StringValue.of("Hello")).value)
            .isEqualTo("echo from server: Hello")
    }
}
