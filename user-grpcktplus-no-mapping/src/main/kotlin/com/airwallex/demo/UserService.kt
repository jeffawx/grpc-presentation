package com.airwallex.demo

import com.airwallex.grpc.error.Error
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import demo.UserOuterClass
import demo.UserServiceRpc
import demo.searchUserResponse
import java.util.UUID
import kotlinx.coroutines.flow.toList
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.stereotype.Service

@Service
class UserService(private val repo: UserRepository) : UserServiceRpc {

    override suspend fun create(request: UserOuterClass.CreateUserRequest): Result<UUID, Error> {
        val name = request.name
        if (name.length < 2 || name.length > 10) {
            return Error.invalid("name should contain 2-10 characters")
        }

        if (name == "admin") {
            return Error.invalid( // gRPC status code inferred from helper method name
                code = "ADMIN_NOT_ALLOWED", // optional application defined code, only for demo purpose
                reason = "cannot create admin",
                details = mapOf("invalid_name" to request.name) // extra data passed back to client, optional
            )
        }

        val type = when (request.type) {
            UserOuterClass.UserType.USER_TYPE_EXTERNAL -> UserType.EXTERNAL
            else -> UserType.INTERNAL
        }

        val email = request.email.takeIf { request.hasEmail() }
        if (email != null && !EmailValidator.getInstance().isValid(email)) {
            return Error.invalid("invalid email address")
        }

        val user = User(null, name, type, email)

        return Ok(repo.save(user).id!!)
    }

    override suspend fun get(request: UUID): Result<UserOuterClass.User, Error> {
        val user = repo.findById(request)

        return if (user != null) {
            Ok(toProto(user))
        } else {
            Error.notFound()
        }
    }

    override suspend fun search(request: UserOuterClass.SearchUserRequest): Result<UserOuterClass.SearchUserResponse, Error> {
        val name = request.name
        if (name.isEmpty()) {
            return Error.invalid("name must be specified for search")
        }

        val userList = repo.findByNameContainingIgnoreCase(name).toList()

        return Ok(searchUserResponse {
            users.addAll(userList.map(::toProto))
        })
    }

    private fun toProto(user: User) = demo.user {
        id = user.id!!.toString()
        name = user.name
        type = when (user.type) {
            UserType.EXTERNAL -> UserOuterClass.UserType.USER_TYPE_EXTERNAL
            else -> UserOuterClass.UserType.USER_TYPE_INTERNAL
        }
        email = user.email ?: ""
    }
}
