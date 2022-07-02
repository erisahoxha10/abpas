package com.abpas.repositories

import com.abpas.entities.ParkingService
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingServiceRepository : CrudRepository<ParkingService, Long> {
}