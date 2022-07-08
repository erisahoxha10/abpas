package com.abpas.controller

import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import com.abpas.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class RaspPiController(
    private val spotRepository: ParkingSpotRepository,
    private val parkingServiceRepository: ParkingServiceRepository,
    private val userRepository: UserRepository
) {


    @GetMapping("")
    fun getMain(): String {
        return "ABPaS Back-end app"

    }

    @PostMapping("/raspPi/startParking")
    fun startParking(@RequestBody spotUserDto: SpotUserDto) {

        //update the parking_spot to state getting bike or state 2
        spotRepository.updateSpot(spotUserDto.parking_spot_id, 2)

        //create a new record in parking service
        var parkingService = ParkingService()
        parkingService.parkingSpot = spotRepository.findByIdOrNull(spotUserDto.parking_spot_id)
        parkingService.user = userRepository.findByIdOrNull(spotUserDto.user_id)
        parkingServiceRepository.save(parkingService)
    }

}