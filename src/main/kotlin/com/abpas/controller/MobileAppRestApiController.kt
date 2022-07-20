package com.abpas.controller

import com.abpas.dto.ParkingSpotResponseDto
import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.entities.ParkingSpot
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import com.abpas.repositories.UserRepository
import java.util.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mobile/api")
class MobileAppRestApiController(
    val parkingServiceRepository: ParkingServiceRepository,
    val parkingSpotRepository: ParkingSpotRepository,
    val userRepository: UserRepository
) {

    @PostMapping("/startParking")
    fun startParking(@RequestBody spotUserDto: SpotUserDto): ParkingSpotResponseDto {
        var res = ParkingSpotResponseDto()
        try {
            //update the parking_spot to state getting bike or state 2
            parkingSpotRepository.updateSpot(spotUserDto.parking_spot_id, 2)

            //create a new record in parking service
            var parkingService = ParkingService()
            parkingService.parkingSpot = parkingSpotRepository.findByIdOrNull(spotUserDto.parking_spot_id)
            parkingService.user = userRepository.findByIdOrNull(spotUserDto.user_id)
            parkingService.state = 1
            parkingServiceRepository.save(parkingService)
            res.serviceId = parkingService.id
        } catch (e: Exception) {
            res.serviceId = 0
        }
        return res
    }

    @PostMapping("/finishParking")
    fun finishParking(@RequestBody spotUserDto: SpotUserDto): ParkingSpotResponseDto {
        var res = ParkingSpotResponseDto()
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.parkingSpot!!.id == spotUserDto.parking_spot_id
                    && it.user!!.id == spotUserDto.user_id
                    && it.state == 3
            }.get(0)
            service.state = 4
            service.arrivalTime = Date()
            parkingServiceRepository.save(service)
            res.serviceId = service.id
        } catch (e: Exception) {
            res.serviceId = 0
        }
        return res
    }

    @PostMapping("/retriveBike")
    fun retrieveBike(@RequestBody spotUserDto: SpotUserDto): ParkingSpotResponseDto {
        var res = ParkingSpotResponseDto()
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.user!!.id == spotUserDto.user_id
                    && it.parkingSpot!!.id == spotUserDto.parking_spot_id
            }.get(0)

            service.state = 7
            parkingServiceRepository.save(service)

            parkingSpotRepository.updateSpot(spotUserDto.parking_spot_id, 4)
            res.serviceId = service.id
        } catch (e: Exception) {
            res.serviceId = 0
        }
        return res
    }

    @PostMapping("/getBike")
    fun getBike(@RequestBody spotUserDto: SpotUserDto): ParkingSpotResponseDto {
        var res = ParkingSpotResponseDto()
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.user!!.id == spotUserDto.user_id
                    && it.parkingSpot!!.id == spotUserDto.parking_spot_id
            }.get(0)

            service.state = 10
            service.departingTime = Date()
            parkingServiceRepository.save(service)

            parkingSpotRepository.updateSpot(spotUserDto.parking_spot_id, 4)

            res.serviceId = service.id
        } catch (e: Exception) {
            res.serviceId = 0
        }
        return res
    }
}