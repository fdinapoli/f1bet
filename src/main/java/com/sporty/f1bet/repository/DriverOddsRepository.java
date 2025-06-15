package com.sporty.f1bet.repository;

import com.sporty.f1bet.model.entity.DriverOdds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverOddsRepository extends JpaRepository<DriverOdds, Long> {
    List<DriverOdds> findBySessionKeyIn(List<Long> sessionKeys);

    DriverOdds findBySessionKeyAndDriverNumber(Long sessionKey, Long driverNumber);
}
