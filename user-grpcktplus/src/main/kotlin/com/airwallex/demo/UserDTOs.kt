package com.airwallex.demo

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class CreateUserRequest(

    @get:Size(min = 2, max = 10, message = "name should contain 2-10 characters")
    val name: String,

    val type: UserType?, // will be null if client is old

    @get:Email
    val email: String?,
)

data class SearchUserRequest(

    @get:NotEmpty
    val name: String
)

data class SearchUserResponse(val users: List<User>)
