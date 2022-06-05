package com.airwallex.demo

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CoroutineCrudRepository<User, UUID> {

    fun findByNameContainingIgnoreCase(name: String): Flow<User>
}
