package com.abpas.controller

import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parking")
class ParkingServiceController(val parkingServiceRepository: ParkingServiceRepository) {

    @PostMapping("/create")
    fun create(@RequestBody user: ParkingService) {
        parkingServiceRepository.save(user)
    }

    @GetMapping("/all")
    fun getAll(): MutableIterable<ParkingService> {
        return parkingServiceRepository.findAll()
    }

}