package com.abpas.controller

import com.abpas.dto.ParkingSpotResponseDto
import com.abpas.dto.SpotUserDto
import com.abpas.entities.ParkingService
import com.abpas.repositories.ParkingServiceRepository
import com.abpas.repositories.ParkingSpotRepository
import com.abpas.repositories.UserRepository
import kotlin.math.log
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/raspPi")
class RaspPiController(
    private val spotRepository: ParkingSpotRepository,
    private val parkingServiceRepository: ParkingServiceRepository,
    private val userRepository: UserRepository
) {

    /**
     * These API are only called by raspPi
     *
     */

    val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("")
    fun getMain(): String {
        return "ABPaS Back-end app"

    }

    /**
     * USE CASE 2
     */
    @GetMapping("/getRoboticInitiativeStates")
    fun getInitiativeStates(): ResponseEntity<ParkingSpotResponseDto> {
        var response = ParkingSpotResponseDto()
        try {
            logger.info("Checking if there are initiative states : parkingSpot.state == 2")
            if (!spotRepository.getInitiativeStates().isEmpty()) {
                logger.info("Intitiative states found")
                //this gets one of the parking spots which is in the initiative state
                var parkingSpot = spotRepository.getInitiativeStates().get(0)
                logger.info("Found parking spot {}", parkingSpot.id)
                //change state of last parking service which has this parking
                //there should be only one parking service with state == 1 with this parking spot id
                var service = parkingServiceRepository.findAll()
                    .filter { it.state == 1 && it.parkingSpot!!.id == parkingSpot!!.id && it.arrivalTime == null }
                    .get(0)
                logger.info("Found service record {}", service.id)
                service.state = 2
                logger.info("Service state changed to 2")
                parkingServiceRepository.save(service)
                response.parkingSpot = parkingSpot.id
                return ResponseEntity.ok(response)
            } else {
                logger.info("No parking spots in state 2")
            }
        } catch (e: Exception) {
            logger.error("An error happened", e.printStackTrace())
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * USE CASE 3
     */
    @GetMapping("/arrived/{parkingSpotId}")
    fun arrived(@PathVariable("parkingSpotId") id: Long): ResponseEntity<Any> {
        try {
            logger.info("Robotic car {} has arrived at the entrance and is signaling this", id)
            logger.info("Looking for the service in state 2 for parking spot {}", id)
            var serviceList =
                parkingServiceRepository.findAll().filter { it.state == 2 && it.parkingSpot!!.id == id }
            if (!serviceList.isEmpty()) {
                var service = serviceList.get(0)
                service.state = 3
                parkingServiceRepository.save(service)
                return ResponseEntity.ok(HttpStatus.OK)
            } else {
                logger.info("There are no services at state 2 for this parking spot ")
            }
        } catch (e: Exception) {
            logger.error("An error happened : ", e.printStackTrace())
        }
        return ResponseEntity.notFound().build()
    }

    /**
     * USE CASE 5
     */
    @GetMapping("/receive")
    fun receive(): ResponseEntity<ParkingSpotResponseDto> {
        logger.info("The user has loaded the bike so the robotic car can go back to its parking spot")
        var response = ParkingSpotResponseDto()
        try {
            logger.info("Looking for the robotic car for which the user has finished parking")
            var service = parkingServiceRepository.findAll().filter { it.state == 4 && it.arrivalTime != null }.get(0)
            service.state = 5
            parkingServiceRepository.save(service)
            response.parkingSpot = service.parkingSpot!!.id
            logger.info("Robotic car is going back to its initial position with a bike!")
        } catch (e: Exception) {
            logger.error(
                "An error happened : could not move robotic car from entrance to its position",
                e.printStackTrace()
            )
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok(response)
    }

    /**
     * USE CASE 6
     */
    @GetMapping("/finished/{parkingSpot}")
    fun finishParking(@PathVariable("parkingSpot") id: Long) {
        logger.info("The robotic car has arrived at initial position with the bike on it and signals this by changing the service state")
        try {
            logger.info("Updating the state of parking service {}", id)
            var service =
                parkingServiceRepository.findAll()
                    .filter { it.parkingSpot!!.id == id && it.state == 5 && it.arrivalTime != null }.get(0)
            service.state = 6
            parkingServiceRepository.save(service)
            logger.info("Updating the state of parking spot {} to 3", id)
            var parkingSpot = spotRepository.updateSpot(id, 3)
        } catch (e: Exception) {
            logger.error("An error happened while finishing parking", e.printStackTrace())
        }
    }

    /**
     * USE CASE 8
     */
    @GetMapping("/getRetrievingState")
    fun retrievingState(): ResponseEntity<ParkingSpotResponseDto> {
        logger.info("Looking for any request to retrieve bike from users")
        var respose = ParkingSpotResponseDto()
        try {
            var service = parkingServiceRepository.findAll().filter {
                it.state == 7 && it.arrivalTime != null
            }.get(0)
            logger.info("Changing state of parking spot {}", service.parkingSpot!!.id)
            service.state = 8
            parkingServiceRepository.save(service)
            respose.parkingSpot == service.parkingSpot!!.id
        } catch (e: Exception) {
            logger.error("An error happened", e.printStackTrace())
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.ok().body(respose)
    }

    /**
     * USE CASE 9
     */
    @GetMapping("/raspPi/deliver/{parkingSpotId}")
    fun deliver(@PathVariable("parkingSpotId") id: Long) {
        logger.info("Robotic car is delivering the bike to the owner from parking spot {}", id)
        try {
            var service =
                parkingServiceRepository.findAll().filter { it.state == 8 && it.parkingSpot!!.id == id }.get(0)
            service.state = 9
            parkingServiceRepository.save(service)
            logger.info("Service state changed to 9")
        } catch (e: Exception) {
            logger.error("Could not deliver bike", e.printStackTrace())
        }
    }

    /**
     * USE CASE 11
     */
    @GetMapping("/return")
    fun returningBike(): ResponseEntity<ParkingSpotResponseDto> {
        var response = ParkingSpotResponseDto()
        try {
            logger.info("Robotic car is returning to its initial position")
            val service =
                parkingServiceRepository.findAll().filter { it.state == 10 && it.departingTime != null }.get(0)
            service.state = 11
            parkingServiceRepository.save(service)
            response.parkingSpot = service.parkingSpot!!.id
        } catch (e: Exception) {
            logger.error("An error happened, ", e.printStackTrace())
        }
        return ResponseEntity.ok(response)
    }

    /**
     * USE CASE 12
     */
    @GetMapping("/done/{parkingSpot}")
    fun doneParking(@PathVariable("parkingSpot") id: Long) {
        logger.info("The robotic car now is at its initial position")
        try {
            logger.info("Set the parking service state to 11")
            var service = parkingServiceRepository.findAll().filter {
                it.parkingSpot!!.id == id
                    && it.state == 10 && it.departingTime != null
            }.get(0)
            service.state = 11
            parkingServiceRepository.save(service)

            logger.info("Setting the parking spot to free")
            //set the parkingSpot to free
            var parkingSpot = spotRepository.updateSpot(id, 1)
        } catch (e: Exception) {
            logger.error("An error happened", e.printStackTrace())
        }
    }

    @PostMapping("/raspPi/startParking")
    fun startParking(@RequestBody spotUserDto: SpotUserDto) {

        //update the parking_spot to state getting bike or state 2
        spotRepository.updateSpot(spotUserDto.parking_spot_id, 2)

        //create a new record in parking service
        var parkingService = ParkingService()
        parkingService.parkingSpot = spotRepository.findByIdOrNull(spotUserDto.parking_spot_id)
        parkingService.user = userRepository.findByIdOrNull(spotUserDto.user_id)
        parkingService.state = 1
        parkingServiceRepository.save(parkingService)

    }


}