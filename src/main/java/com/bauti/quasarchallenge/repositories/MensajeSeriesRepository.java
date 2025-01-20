package com.bauti.quasarchallenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bauti.quasarchallenge.entities.MensajeSeries;

@Repository
public interface MensajeSeriesRepository extends JpaRepository<MensajeSeries, Long>{
    MensajeSeries findFirstByOrderByIdDesc();
}
