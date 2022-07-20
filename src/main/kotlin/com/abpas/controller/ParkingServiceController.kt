package com.abpas.controller

import com.abpas.dto.ParkingSpotResponseDto
import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.entities.ParkingSpot
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

    @GetMapping("state3")
    fun checkBikeArrived(): ParkingSpotResponseDto? {
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.state == 3 && it.arrivalTime == null
            }.get(0)
            var res = ParkingSpotResponseDto()
            res.parkingSpot = service.parkingSpot?.id
            return res
        } catch (e: Exception) {
            return null
        }
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

    @PostMapping("/retriveBike")
    fun retrieveBike(@RequestBody spotUserDto: SpotUserDto) {
        var service = parkingServiceRepository.findAll().filter {
            it.user!!.id == spotUserDto.user_id
                && it.parkingSpot!!.id == spotUserDto.parking_spot_id
        }.get(0)

        service.state = 7
        parkingServiceRepository.save(service)

        parkingSpotRepository.updateSpot(spotUserDto.parking_spot_id, 4)
    }

    @PostMapping("/getBike")
    fun getBike(@RequestBody spotUserDto: SpotUserDto) {
        var service = parkingServiceRepository.findAll().filter {
            it.user!!.id == spotUserDto.user_id
                && it.parkingSpot!!.id == spotUserDto.parking_spot_id
        }.get(0)

        service.state = 10
        parkingServiceRepository.save(service)

        parkingSpotRepository.updateSpot(spotUserDto.parking_spot_id, 4)
    }

}