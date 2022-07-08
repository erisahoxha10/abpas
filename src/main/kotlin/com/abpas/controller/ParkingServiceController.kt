package com.abpas.controller

import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import java.util.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/parking")
class ParkingServiceController(
    val parkingServiceRepository: ParkingServiceRepository,
    val parkingSpotRepository: ParkingSpotRepository
) {

    @PostMapping("/create")
    fun create(@RequestBody user: ParkingService) {
        parkingServiceRepository.save(user)
    }

    @GetMapping("/all")
    fun getAll(): MutableIterable<ParkingService> {
        return parkingServiceRepository.findAll()
    }

    @PostMapping("/finishParking")
    fun finishParking(@RequestBody spotUserDto: SpotUserDto) {
        var service = parkingServiceRepository.findAll().filter {
            it.parkingSpot!!.id == spotUserDto.parking_spot_id
                && it.user!!.id == spotUserDto.user_id
        }.get(0)
        service.state = 4
        service.arrivalTime = Date()
        parkingServiceRepository.save(service)
    }

    @GetMapping("/receive")
    fun receive(): String {
        var service = parkingServiceRepository.findAll().filter { it.state == 4 }.get(0)
        service.state = 5
        parkingServiceRepository.save(service)
        return "{\"parkingSpot\":" + service.parkingSpot!!.id + "}"
    }

    @GetMapping("/finished/{parkingSpot}")
    fun finishParking(@PathVariable("parkingSpot") id: Long) {
        var service = parkingServiceRepository.findAll().filter { it.parkingSpot!!.id == id }.get(0)
        service.state = 6
        parkingServiceRepository.save(service)

        var parkingSpot = parkingSpotRepository.updateSpot(id, 3)
    }
}