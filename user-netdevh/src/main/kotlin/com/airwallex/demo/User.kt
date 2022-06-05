package com.airwallex.demo

import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    @Id
    val id: UUID?,
    val name: String,
    val type: UserType,
    val email: String?,
)

enum class UserType { INTERNAL, EXTERNAL }
