package com.playgrounds.repository;

import com.playgrounds.models.ParkOpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParkOpeningHoursRepository extends JpaRepository<ParkOpeningHours, Long> {

}
