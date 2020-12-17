package com.playgrounds.repository;

import com.playgrounds.models.ParkFacility;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParkFacilityRepository extends JpaRepository<ParkFacility, Long> {

}
