package com.playgrounds.repository;


import com.playgrounds.models.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface CountyRepository extends JpaRepository<County, Long> {
    County findByCounty(String county);

    boolean existsByCounty(String county);

    @Transactional
    @Modifying
    @Query("update County c set c.county = ?2 where c.id = ?1")
    void updateCountyName(Long countyId, String countyName);
}
