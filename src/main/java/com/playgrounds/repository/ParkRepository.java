package com.playgrounds.repository;


import com.playgrounds.models.Park;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Long> {

    List<Park> findAllByCountyId(Long countyId);

    Park findByName(String name);

    boolean existsByName(String name);

}
