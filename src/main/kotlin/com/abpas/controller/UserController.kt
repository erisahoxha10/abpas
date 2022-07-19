package com.abpas.controller

import com.abpas.dto.UserDto
import com.abpas.dto.UserLoginDto
import com.abpas.entities.User
import com.abpas.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(val userRepository: UserRepository) {

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Long): User? {
        println("user asked from mobile")
        return userRepository.findByIdOrNull(id)
    }

    @PostMapping("/create")
    fun create(@RequestBody user: User) {
        userRepository.save(user)
    }

    @GetMapping("/all")
    fun getAll(): MutableIterable<User> {
        return userRepository.findAll()
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginData: UserLoginDto): UserDto? {
        val password = userLoginData.password
        if (userLoginData.name_email?.contains("@") == true) {
            var email = userLoginData.name_email
            return userRepository.findByEmailAndPassword(email!!, password!!)
        } else {
            var username = userLoginData.name_email
            return userRepository.findByUsernameAndPassword(username!!, password!!)
        }
    }
}