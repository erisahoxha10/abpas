package com.abpas.controller

import com.abpas.entities.ParkingSpot
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parkingSpot")
class ParkingSpotController(
    val parkingSpotRepository: ParkingSpotRepository,
    val parkingServiceRepository: ParkingServiceRepository
) {

    @PostMapping("/create")
    fun create(@RequestBody user: ParkingSpot) {
        parkingSpotRepository.save(user)
    }

    @GetMapping("/all")
    fun getAll(): MutableIterable<ParkingSpot> {
        return parkingSpotRepository.findAll()
    }


}