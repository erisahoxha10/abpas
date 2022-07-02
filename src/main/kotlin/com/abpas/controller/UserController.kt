package com.abpas.controller

import com.abpas.entities.User
import com.abpas.repositories.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(val userRepository: UserRepository) {

    @PostMapping("/create")
    fun create(@RequestBody user: User) {
        userRepository.save(user)
    }

    @GetMapping("/all")
    fun getAll(): MutableIterable<User> {
        return userRepository.findAll()
    }
}