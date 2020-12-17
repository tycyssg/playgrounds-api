package com.playgrounds.repository;


import com.playgrounds.models.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Long> {

    List<Park> findAllByCountyId(Long countyId);

    Park findByName(String name);

    boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("update Park p set p.name = ?1, p.description = ?2, p.phone = ?3,p.email = ?4, p.website = ?5, p.accessibility = ?6, p.directions = ?7, p.surfaceType = ?8 where p.id = ?9")
    void updatePark(String name, String description, String phone, String email, String website, String accessibility, String directions, String surfaceType, Long parkId);

}
