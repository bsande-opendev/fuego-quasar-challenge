package com.bauti.quasarchallenge.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bauti.quasarchallenge.entities.Satelite;

public interface SateliteRepository extends JpaRepository<Satelite, Long>{
    
    Optional<Satelite> findByNameIgnoreCase(String name);
}
