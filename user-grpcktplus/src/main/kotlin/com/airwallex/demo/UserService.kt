package com.airwallex.demo

import com.airwallex.grpc.error.Error
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import demo.UserServiceRpc
import java.util.UUID
import javax.validation.Valid
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository) : UserServiceRpc {

    override suspend fun create(@Valid request: CreateUserRequest): Result<UUID, Error> {
        if (request.name == "admin") {
            return Error.invalid( // gRPC status code inferred from helper method name
                code = "ADMIN_NOT_ALLOWED", // optional application defined code, only for demo purpose
                reason = "cannot create admin",
                details = mapOf("invalid_name" to request.name) // extra data passed back to client, optional
            )
        }

        val user = User(null, request.name, request.type ?: UserType.INTERNAL, request.email)

        return Ok(repo.save(user).id!!)
    }

    override suspend fun get(request: UUID): Result<User, Error> {
        val user = repo.findById(request)

        return if (user != null) {
            Ok(user)
        } else {
            Error.notFound()
        }
    }

    override suspend fun search(@Valid request: SearchUserRequest): Result<SearchUserResponse, Error> {
        val users = repo.findByNameContainingIgnoreCase(request.name).toList()
        return Ok(SearchUserResponse(users))
    }
}
