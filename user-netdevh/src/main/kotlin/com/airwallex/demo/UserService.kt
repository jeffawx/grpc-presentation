package com.airwallex.demo

import com.google.protobuf.StringValue
import demo.UserOuterClass
import demo.UserServiceGrpcKt
import demo.searchUserResponse
import demo.user
import io.grpc.Status
import java.util.UUID
import kotlinx.coroutines.flow.toList
import net.devh.boot.grpc.server.service.GrpcService
import org.apache.commons.validator.routines.EmailValidator

@GrpcService
class UserService(private val repo: UserRepository) : UserServiceGrpcKt.UserServiceCoroutineImplBase() {

    override suspend fun create(request: UserOuterClass.CreateUserRequest): StringValue {
        val name = request.name
        if (name.length < 2 || name.length > 10) {
            throw Status.INVALID_ARGUMENT.withDescription("name should contain 2-10 characters").asRuntimeException()
        }

        if (name == "admin") {
            throw Status.INVALID_ARGUMENT.withDescription("cannot create admin").asRuntimeException()
        }

        val type = when (request.type) {
            UserOuterClass.UserType.USER_TYPE_EXTERNAL -> UserType.EXTERNAL
            else -> UserType.INTERNAL
        }

        val email = request.email.takeIf { request.hasEmail() }
        if (email != null && !EmailValidator.getInstance().isValid(email)) {
            throw Status.INVALID_ARGUMENT.withDescription("invalid email address").asRuntimeException()
        }

        val user = User(null, name, type, email)

        return StringValue.of(repo.save(user).id!!.toString())
    }

    override suspend fun get(request: StringValue): UserOuterClass.User {
        val userId = try {
            UUID.fromString(request.value)
        } catch (e: Exception) {
            throw Status.INVALID_ARGUMENT.withDescription("user id must be uuid").withCause(e).asRuntimeException()
        }

        val user = repo.findById(userId) ?: throw Status.NOT_FOUND.withDescription("user not found").asRuntimeException()

        return toProto(user)
    }

    override suspend fun search(request: UserOuterClass.SearchUserRequest): UserOuterClass.SearchUserResponse {
        val name = request.name
        if (name.isEmpty()) {
            throw Status.INVALID_ARGUMENT.withDescription("name must be specified for search").asRuntimeException()
        }

        val userList = repo.findByNameContainingIgnoreCase(name).toList()

        return searchUserResponse {
            users.addAll(userList.map(::toProto))
        }
    }

    private fun toProto(user: User) = user {
        id = user.id!!.toString()
        name = user.name
        type = when (user.type) {
            UserType.EXTERNAL -> UserOuterClass.UserType.USER_TYPE_EXTERNAL
            else -> UserOuterClass.UserType.USER_TYPE_INTERNAL
        }
        email = user.email ?: ""
    }
}
