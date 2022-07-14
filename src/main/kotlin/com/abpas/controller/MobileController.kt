package com.abpas.controller

import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import com.abpas.repositories.UserRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mobile")
class MobileController(
    private val spotRepository: ParkingSpotRepository,
    private val parkingServiceRepository: ParkingServiceRepository,
    private val userRepository: UserRepository
) {

}