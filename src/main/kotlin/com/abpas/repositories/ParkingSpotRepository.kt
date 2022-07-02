package com.abpas.repositories

import com.abpas.entities.ParkingSpot
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ParkingSpotRepository : CrudRepository<ParkingSpot, Long> {
}