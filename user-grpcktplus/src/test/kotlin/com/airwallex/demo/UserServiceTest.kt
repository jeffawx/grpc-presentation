package com.airwallex.demo

import com.airwallex.grpc.annotations.GrpcClient
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
import demo.UserServiceRpc
import io.grpc.Status
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    "grpc.server.name=test",
    "grpc.server.port=-1", // -1 for in-process server
    "grpc.client.channels.userClient.in-process=true",
    "grpc.client.channels.userClient.server-name=test"
)
class UserServiceTest {

    @Autowired
    @GrpcClient
    private lateinit var userClient: UserServiceRpc

    @Test
    fun `cannot create admin user`() = runBlocking<Unit> {
        val user = CreateUserRequest(name = "admin", type = UserType.EXTERNAL, email = null)

        val result = userClient.create(user)
        assertThat(result.get()).isNull()

        val error = result.getError()
        assertNotNull(error)
        assertThat(error.statusCode).isEqualTo(Status.INVALID_ARGUMENT.code)
        assertThat(error.description).isEqualTo("cannot create admin")
        assertThat(error.details["invalid_name"]).isEqualTo("admin")
    }

    @Test
    fun `name is invalid`() = runBlocking<Unit> {
        val user = CreateUserRequest(name = "h", type = UserType.EXTERNAL, email = null)

        val result = userClient.create(user)
        assertThat(result.get()).isNull()

        val error = result.getError()
        assertNotNull(error)
        assertThat(error.statusCode).isEqualTo(Status.INVALID_ARGUMENT.code)
        assertThat(error.details["x-validate-name"]).isEqualTo("name should contain 2-10 characters")
    }
}
