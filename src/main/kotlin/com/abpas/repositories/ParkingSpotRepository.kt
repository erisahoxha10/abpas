package com.abpas.repositories

import com.abpas.entities.ParkingSpot
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ParkingSpotRepository : CrudRepository<ParkingSpot, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update ParkingSpot s set s.state=:state where s.id=:parking_spot_id")
    fun updateSpot(@Param(value = "parking_spot_id") parking_spot_id: Long, @Param(value = "state") state: Int)
}