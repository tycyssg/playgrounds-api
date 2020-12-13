package com.playgrounds.repository;


import com.playgrounds.models.County;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountyRepository extends JpaRepository<County, Long> {

}
