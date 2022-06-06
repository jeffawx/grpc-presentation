package com.airwallex.demo

import com.airwallex.grpc.error.Error
import com.github.michaelbull.result.Result
import demo.CreateUserRequest
import demo.SearchUserRequest
import demo.SearchUserResponse
import demo.User
import demo.UserServiceRpc
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class UserService : UserServiceRpc {

    override suspend fun create(request: CreateUserRequest): Result<UUID, Error> {
        TODO("Not yet implemented")
    }

    override suspend fun get(request: UUID): Result<User, Error> {
        TODO("Not yet implemented")
    }

    override suspend fun search(request: SearchUserRequest): Result<SearchUserResponse, Error> {
        TODO("Not yet implemented")
    }
}
