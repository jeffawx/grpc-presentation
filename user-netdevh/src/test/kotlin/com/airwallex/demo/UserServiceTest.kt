package com.airwallex.demo

import demo.UserServiceGrpcKt
import demo.createUserRequest
import io.grpc.Status
import io.grpc.StatusException
import kotlin.test.Test
import kotlinx.coroutines.runBlocking
import net.devh.boot.grpc.client.inject.GrpcClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    "grpc.server.in-process-name=test",
    "grpc.client.user.address=in-process:test"
)
class UserServiceTest {

    @GrpcClient("user")
    private lateinit var userClient: UserServiceGrpcKt.UserServiceCoroutineStub

    @Test
    fun `cannot create admin user`() = runBlocking<Unit> {
        val user = createUserRequest { name = "admin" }

        val exception = assertThrows<StatusException> {
            userClient.create(user)
        }
        assertThat(exception.status.code).isEqualTo(Status.INVALID_ARGUMENT.code)
        assertThat(exception.message).contains("cannot create admin")
    }
}
