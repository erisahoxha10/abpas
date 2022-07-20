package com.abpas.controller

import com.abpas.dto.ParkingSpotResponseDto
import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/state3")
    fun checkBikeArrived(): ParkingSpotResponseDto {
        var res = ParkingSpotResponseDto()
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.state == 3 && it.arrivalTime == null
            }.get(0)
            res.serviceId = service.id
            return res
        } catch (e: Exception) {
            res.serviceId = 0
            return res
        }
    }
}