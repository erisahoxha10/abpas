package com.abpas.controller

import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import com.abpas.repositories.UserRepository
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mobile")
class MobileController(
    private val spotRepository: ParkingSpotRepository,
    private val parkingServiceRepository: ParkingServiceRepository,
    private val userRepository: UserRepository
) {


    @GetMapping("/startParking/{parking_spot}/{user_id}")
    fun startParking(@PathVariable("parking_spot") parking_spot: Long, @PathVariable("user_id") user_id: Long) {

        //update the parking_spot to state getting bike or state 2
        spotRepository.updateSpot(parking_spot, 2)

        //create a new record in parking service
        var parkingService = ParkingService()
        parkingService.parkingSpot = spotRepository.findByIdOrNull(parking_spot)
        parkingService.user = userRepository.findByIdOrNull(user_id)
        parkingService.state = 1
        parkingServiceRepository.save(parkingService)

    }

    @GetMapping("/finishParking/{parking_spot}/{user_id}")
    fun finishParking(@PathVariable("parking_spot") parking_spot: Long, @PathVariable("user_id") user_id: Long) {
        var service = parkingServiceRepository.findAll().filter {
            it.parkingSpot!!.id == parking_spot
                && it.user!!.id == user_id
        }.get(0)
        service.state = 4
        service.arrivalTime = Date()
        parkingServiceRepository.save(service)
    }

    @PostMapping("/retriveBike/{parking_spot}/{user_id}")
    fun retrieveBike(@PathVariable("parking_spot") parking_spot: Long, @PathVariable("user_id") user_id: Long) {
        var service = parkingServiceRepository.findAll().filter {
            it.user!!.id == user_id
                && it.parkingSpot!!.id == parking_spot
        }.get(0)

        service.state = 7
        parkingServiceRepository.save(service)

        spotRepository.updateSpot(parking_spot, 4)
    }

    @PostMapping("/getBike/{parking_spot}/{user_id}")
    fun getBike(@PathVariable("parking_spot") parking_spot: Long, @PathVariable("user_id") user_id: Long) {
        var service = parkingServiceRepository.findAll().filter {
            it.user!!.id == user_id
                && it.parkingSpot!!.id == parking_spot
        }.get(0)

        service.state = 10
        parkingServiceRepository.save(service)

        spotRepository.updateSpot(parking_spot, 4)
    }

}