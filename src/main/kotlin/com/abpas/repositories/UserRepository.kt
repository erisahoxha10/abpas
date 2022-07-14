package com.abpas.repositories

import com.abpas.dto.UserDto
import com.abpas.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {

    fun findByUsernameAndPassword(username: String, password: String): UserDto?

    fun findByEmailAndPassword(email: String, password: String): UserDto?
}