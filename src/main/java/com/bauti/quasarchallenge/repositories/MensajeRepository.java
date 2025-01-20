package com.bauti.quasarchallenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bauti.quasarchallenge.entities.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long>{
    
}
